package manager.trade.techno.trademanager;

/**
 * Created by jay on 22-Jul-16.
 */

import java.util.ArrayList;
import java.util.List;

import com.cleveroad.loopbar.R;

import com.cleveroad.loopbar.adapter.ICategoryItem;

public class MockedItemsFactory_Custom {
    public static List<ICategoryItem> getCategoryItems() {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.live_market_icon, "item1"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.top_gainer, "item2"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.top_loser, "item3"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.tips_icon, "item4"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.aboutus, "item5"));
        return items;
    }

    public static List<ICategoryItem> getCategoryItemsUniq() {
        List<ICategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.live_market_icon, "Live"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.top_gainer, "Top Gainer"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.top_loser, "Top Loser"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.tips_icon, "Tips"));
        items.add(new CategoryItem_Custom(manager.trade.techno.trademanager.R.drawable.aboutus, "About\nUs"));

        return items;
    }
}
