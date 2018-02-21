package com.casper.creitive.WCFHandlers.Communication;

/**
 * Created by Desu on 2018-02-21.
 */

public interface WCFResponse
{
    void getResponse(String response);
    void onError(String response, int code);
}
