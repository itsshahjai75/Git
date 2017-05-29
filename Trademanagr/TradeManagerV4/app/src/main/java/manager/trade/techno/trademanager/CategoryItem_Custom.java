package manager.trade.techno.trademanager;

import com.cleveroad.loopbar.adapter.ICategoryItem;

/**
 * Created by jay on 22-Jul-16.
 */
public class CategoryItem_Custom implements ICategoryItem {
    private int categoryItemDrawableId;
    private String categoryName;

    public CategoryItem_Custom(int categoryItemDrawableId, String categoryName) {
        this.categoryItemDrawableId = categoryItemDrawableId;
        this.categoryName = categoryName;
    }

    @Override
    public int getCategoryIconDrawable() {
        return categoryItemDrawableId;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CategoryItem_Custom && ((CategoryItem_Custom) o).categoryName.equals(categoryName);
    }
}
