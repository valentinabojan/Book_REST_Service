package org.library.business_layer.value_object;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorBean {

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}