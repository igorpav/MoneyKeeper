package team.e.com.moneykeeper.fragments;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import team.e.com.moneykeeper.providers.ExpensesContract.Expenses;
import team.e.com.moneykeeper.providers.ExpensesContract.ExpensesWithCategories;
import team.e.com.moneykeeper.R;
import team.e.com.moneykeeper.adapters.SimpleExpenseAdapter;
import team.e.com.moneykeeper.utils.Utils;
import team.e.com.moneykeeper.activities.ExpenseEditActivity;

import java.util.Date;

public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SUM_LOADER_ID = 0;
    private static final int LIST_LOADER_ID = 1;

    private ListView mExpensesView;
    private View mProgressBar;
    private SimpleExpenseAdapter mAdapter;
    private TextView mTotalExpSumTextView;
    private TextView mTotalExpCurrencyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        mExpensesView = (ListView) rootView.findViewById(R.id.expenses_list_view);
        mProgressBar = rootView.findViewById(R.id.expenses_progress_bar);
        mTotalExpSumTextView = (TextView) rootView.findViewById(R.id.total_expense_sum_text_view);
        mTotalExpCurrencyTextView = (TextView) rootView.findViewById(R.id.total_expense_currency_text_view);

        mExpensesView.setEmptyView(rootView.findViewById(R.id.expenses_empty_list_view));
        mExpensesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prepareExpenseToEdit(id);
            }
        });

        mTotalExpSumTextView.setText(Utils.formatToCurrency(0.0f));

        registerForContextMenu(mExpensesView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new SimpleExpenseAdapter(getActivity());
        mExpensesView.setAdapter(mAdapter);

        getLoaderManager().initLoader(SUM_LOADER_ID, null, this);
        getLoaderManager().initLoader(LIST_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadExpenseData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_today, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_expense_menu_item:
                prepareExpenseToCreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.expense_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_expense_menu_item:
                deleteExpense(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id) {
            case SUM_LOADER_ID:
                uri = ExpensesWithCategories.SUM_DATE_CONTENT_URI;
                break;
            case LIST_LOADER_ID:
                uri = ExpensesWithCategories.DATE_CONTENT_URI;
                break;
        }

        String today = Utils.getDateString(new Date());
        String[] selectionArgs = { today };

        return new CursorLoader(getActivity(),
                uri,
                null,
                null,
                selectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case SUM_LOADER_ID:
                int valueSumIndex = data.getColumnIndex(Expenses.VALUES_SUM);
                data.moveToFirst();
                float valueSum = data.getFloat(valueSumIndex);
                mTotalExpSumTextView.setText(Utils.formatToCurrency(valueSum));
                break;

            case LIST_LOADER_ID:
                mProgressBar.setVisibility(View.GONE);
                mAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case SUM_LOADER_ID:
                mTotalExpSumTextView.setText(Utils.formatToCurrency(0.0f));
                break;
            case LIST_LOADER_ID:
                mAdapter.swapCursor(null);
                break;
        }
    }

    private void reloadExpenseData() {
        mProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(LIST_LOADER_ID, null, this);
        getLoaderManager().restartLoader(SUM_LOADER_ID, null, this);
    }

    private int deleteSingleExpense(long expenseId) {
        Uri uri = ContentUris.withAppendedId(Expenses.CONTENT_URI, expenseId);
        int rowsDeleted;

        rowsDeleted = getActivity().getContentResolver().delete(
                uri,
                null,
                null
        );

        showStatusMessage(getResources().getString(R.string.expense_deleted));
        reloadExpenseData();

        return rowsDeleted;
    }

    private void deleteExpense(final long expenseId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_expense)
                .setMessage(R.string.delete_exp_dialog_msg)
                .setNeutralButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSingleExpense(expenseId);
                    }
                })
                .show();
    }

    private void showStatusMessage(CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private void prepareExpenseToCreate() {
        startActivity(new Intent(getActivity(), ExpenseEditActivity.class));
    }

    private void prepareExpenseToEdit(long id) {
        Intent intent = new Intent(getActivity(), ExpenseEditActivity.class);
        intent.putExtra(ExpenseEditFragment.EXTRA_EDIT_EXPENSE, id);
        startActivity(intent);
    }

}
