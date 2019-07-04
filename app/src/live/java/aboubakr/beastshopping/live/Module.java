package aboubakr.beastshopping.live;

import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;

public class Module {
    public static void Register(BeastShoppingApplication application){
        new LiveAccountServices(application);
        new LiveShoppingService(application);
    }
}
