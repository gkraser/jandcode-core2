package jandcode.web.gsp;

/**
 * Предок для утилит gsp.
 * <p>
 * Экземпляры таких классов обычно создаются в gsp <code>MyGsp th = new MyGsp(this)</code>.
 * После этого переменную th можно использовать как набор утилит в gsp.
 * <p>
 * Сделано, в том числе, для обеспечения удобной работы с подсказками в ide.
 * <p>
 * При наследовании обязательно нужно перекрывать конструктор с параметром delegate.
 */
public class GspUtils extends BaseGsp {

    public GspUtils(Gsp delegate) {
        setDelegate(delegate);
    }

    // dummy
    protected final void onRender() throws Exception {
    }

}
