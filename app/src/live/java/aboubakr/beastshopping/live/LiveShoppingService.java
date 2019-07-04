package aboubakr.beastshopping.live;

import android.widget.Toast;

import com.aboubakr.beastshopping.entities.ShoppingList;
import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;
import com.aboubakr.beastshopping.infrastructure.Utils;
import com.aboubakr.beastshopping.services.ShoppingListService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

public class LiveShoppingService extends BaseLiveService {
    public LiveShoppingService(BeastShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void AddshoppingList(ShoppingListService.AddShoppingListRequest request){
        ShoppingListService.AddShoppingListResponse response = new ShoppingListService.AddShoppingListResponse();
        if(request.shoppingListName.isEmpty()){
            response.setPropertyErrors("listName", "Shopping list must have a name");
        }
        if(response.didSucceed()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(Utils.FIREBASE_SHOPPING_LIST_REFERENCE + request.ownerEmail).push();
            HashMap<String,Object> timestampedCreated = new HashMap<>();
            timestampedCreated.put("timestamp", ServerValue.TIMESTAMP);
            ShoppingList shoppingList = new ShoppingList(reference.getKey(),
                    request.shoppingListName,
                    Utils.decodeEmail(request.ownerEmail),
                    request.ownerName,
                    timestampedCreated);
            reference.child("id").setValue(shoppingList.getId());
            reference.child("listName").setValue(shoppingList.getListName());
            reference.child("ownerEmail").setValue(shoppingList.getOwnerEmail());
            reference.child("ownerName").setValue(shoppingList.getOwnerName());
            reference.child("dateCreated").setValue(shoppingList.getDateCreated());
            reference.child("dateLastChanged").setValue(shoppingList.getDateLastChanged());



            Toast.makeText(application.getApplicationContext(),
                    "List has been created.",
                    Toast.LENGTH_LONG).show();
        }
        bus.post(response);
    }
}
