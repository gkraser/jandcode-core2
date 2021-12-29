package jandcode.core.dbm.ddl.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.core.dbm.ddl.*;
import jandcode.core.dbm.ddl.impl.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.store.*;
import org.apache.commons.vfs2.*;

import java.util.*;

public class DDLProvider_dbdata extends BaseDDLProvider {

    private boolean used;
    private String path;

    class FieldLink {
        Field domainField;
        StoreField storeField;

    }

    protected void onLoad(List<DDLOper> res, DDLStage stage) throws Exception {
        if (used) {
            return; // должно отрабатывать только один раз
        }
        used = true;
        //

        String path = getPath();
        if (UtString.empty(path)) {
            return; // нет пути
        }

        StoreService svcStore = getApp().bean(StoreService.class);
        DomainService svcDomain = getModel().bean(DomainService.class);
        DDLService svcDLL = getModel().bean(DDLService.class);

        DirScanner<FileObject> sc = UtFile.createDirScannerVfs(path);
        List<FileObject> lst = sc.load();

        for (FileObject f : lst) {
            String fn = f.toString();
            String ldrName = DDLUtils.fileNameToStoreLoaderName(fn);
            if (!svcStore.hasStoreLoader(ldrName)) {
                throw new XError("Для файла {0} не найден StoreLoader: {1}", fn, ldrName);
            }
            //
            String domainName = DDLUtils.fileNameToDomainName(fn);
            Domain domain = svcDomain.findDomain(domainName);
            if (domain == null) {
                throw new XError("Для файла {0} не найден Domain: {1}", fn, ldrName);
            }
            StoreLoader ldr = svcStore.createStoreLoader(ldrName);
            Store store = svcDomain.createStore(domain);
            ldr.setStore(store);
            ldr.load().fromFileObject(f);

            List<String> fields = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<FieldLink> links = new ArrayList<>();

            for (Field fld : domain.getFields()) {
                FieldLink link = new FieldLink();
                link.domainField = fld;
                link.storeField = store.findField(fld.getName());
                if (link.storeField == null) {
                    continue;
                }
                links.add(link);
                fields.add(fld.getName());
            }
            String sqlBegin = "insert into " + domain.getName() +
                    " (" + UtString.join(fields, ",") + ")\nvalues (";

            for (StoreRecord rec : store) {
                values.clear();
                for (FieldLink link : links) {
                    values.add(link.domainField.getSqlValue(rec.getValueNullable(link.storeField.getIndex())));
                }

                String sql = sqlBegin + UtString.join(values, ",") + ")";
                DDLOper_sql a = (DDLOper_sql) svcDLL.createOperInst("sql");
                a.setSqlText(sql);
                a.setName(getBaseName() + domainName);

                res.add(a);
            }
        }

    }

    //////

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
