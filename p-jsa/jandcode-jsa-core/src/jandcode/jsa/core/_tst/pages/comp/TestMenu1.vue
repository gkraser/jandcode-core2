<template>
    <jc-menu>
        <jc-action icon="bus" text="01 Hello"/>
        <jc-action text="02 Hello Hello Hello ">
            <jc-action text="03 Hello"/>
            <jc-action text="04 Hello Hello Hello ">
                <jc-action text="05 Hello"/>
                <jc-action text="06 Hello Hello Hello ">
                    <jc-action text="07 Hello"/>
                    <jc-action text="08 Hello Hello Hello "/>
                    <jc-action text="09 Hello Hello Hello "/>
                </jc-action>
                <jc-action text="10 Hello Hello Hello "/>
                <jc-action text="11 Hello Hello Hello "/>
            </jc-action>
            <jc-action text="12 Hello Hello Hello "/>
            <jc-action text="13 Hello Hello Hello "/>
        </jc-action>
        <jc-action text="16 One sub item">
            <jc-action text="14 Hello"/>
        </jc-action>
        <jc-action text="17 Custom popup">
            <div style="padding: 50px;background-color: #e8eaf6">
                CUSTOM POPUP
            </div>
        </jc-action>
        <jc-separator/>
        <jc-action icon="bus" text="15 Hello Hello Hello "/>
    </jc-menu>
</template>

<script>
    export default {
        props: {},
        data() {
            return {}
        }
    }
</script>
