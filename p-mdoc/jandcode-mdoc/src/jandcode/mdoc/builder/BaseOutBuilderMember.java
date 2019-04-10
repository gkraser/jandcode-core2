package jandcode.mdoc.builder;

/**
 * Предок для объектов в контексте OutBuilder
 */
public abstract class BaseOutBuilderMember implements IOutBuilderLinkSet {

    private OutBuilder outBuilder;

    public OutBuilder getOutBuilder() {
        return outBuilder;
    }

    public void setOutBuilder(OutBuilder outBuilder) {
        this.outBuilder = outBuilder;
    }

}
