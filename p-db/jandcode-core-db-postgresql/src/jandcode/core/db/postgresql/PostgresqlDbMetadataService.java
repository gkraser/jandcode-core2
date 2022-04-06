package jandcode.core.db.postgresql;


import jandcode.core.db.std.*;

public class PostgresqlDbMetadataService extends BaseDbMetadataService {

    protected String normalizeCase(String name) {
        return name.toLowerCase();
    }
}
