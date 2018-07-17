package team.e.com.moneykeeper.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team.e.com.moneykeeper.providers.ExpensesContract;
import team.e.com.moneykeeper.R;
import team.e.com.moneykeeper.utils.Utils;

public class SimpleExpenseAdapter extends CursorAdapter {
    private String mCurrency;

    public SimpleExpenseAdapter(Context context) {
        super(context, null, 0);
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expense_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvExpenseValue = (TextView) view.findViewById(R.id.expense_value_text_view);
        TextView tvExpenseCurrency = (TextView) view.findViewById(R.id.expense_currency_text_view);
        TextView tvExpenseCatName = (TextView) view.findViewById(R.id.expense_category_name_text_view);

        float expValue = cursor.getFloat(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.VALUE));
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Categories.NAME));

        tvExpenseValue.setText(Utils.formatToCurrency(expValue));
        tvExpenseCatName.setText(categoryName);
        tvExpenseCurrency.setText(mCurrency);
    }
}
