package com.getbase.android.db.cursors;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

import android.database.Cursor;
import android.database.CursorWrapper;

public class FluentCursor extends CursorWrapper {

  public FluentCursor(Cursor cursor) {
    super(Cursors.returnSameOrEmptyIfNull(cursor));
  }

  /**
   * Transforms Cursor to FluentIterable of T applying given function
   * WARNING: This method closes cursor. Do not use this from onLoadFinished()
   *
   * @param singleRowTransform Function to apply on every single row of this cursor
   * @param <T> Type of Iterable's single element
   * @return Transformed iterable
   */
  public <T> FluentIterable<T> toFluentIterable(Function<? super Cursor, T> singleRowTransform) {
    try {
      return Cursors.toFluentIterable(this, singleRowTransform);
    } finally {
      close();
    }
  }

  /**
   * Returns the first row of this cursor transformed using the given function.
   * WARNING: This method closes cursor. Do not use this from onLoadFinished()
   *
   * @param singleRowTransform Function to apply on every single row of this cursor
   * @param <T> Type of Iterable's single element
   * @return Transformed first row of the cursor. If the cursor was empty,
   * {@code Optional.absent()} is returned.
   */
  public <T> Optional<T> toFirstRow(Function<? super Cursor, T> singleRowTransform) {
    try {
      if (moveToFirst()) {
        return Optional.fromNullable(singleRowTransform.apply(this));
      } else {
        return Optional.absent();
      }
    } finally {
      close();
    }
  }

  /**
   * Returns number of rows in this cursor and closes it.
   * WARNING: This method closes cursor. Do not use this from onLoadFinished()
   *
   * @return Row count from this cursor
   */
  public int toRowCount() {
    try {
      return getCount();
    } finally {
      close();
    }
  }
}
