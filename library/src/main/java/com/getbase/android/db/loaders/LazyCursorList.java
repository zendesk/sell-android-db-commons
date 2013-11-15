package com.getbase.android.db.loaders;

import com.getbase.android.db.cursors.Cursors;
import com.google.common.base.Function;

import android.database.Cursor;
import android.support.v4.util.LruCache;

import java.util.AbstractList;

class LazyCursorList<T> extends AbstractList<T> {

  private final Cursor cursor;
  private final Function<Cursor, T> transformation;
  private final LruCache<Integer, T> cache;

  public LazyCursorList(Cursor cursor, Function<Cursor, T> function) {
    this(cursor, function, 256);
  }

  public LazyCursorList(final Cursor cursor, Function<Cursor, T> function, int cacheSize) {
    this.cursor = Cursors.returnSameOrEmptyIfNull(cursor);
    this.transformation = function;

    cache = new LruCache<Integer, T>(cacheSize) {
      @Override
      protected T create(Integer key) {
        cursor.moveToPosition(key);
        return transformation.apply(cursor);
      }
    };
  }

  @Override
  public T get(int i) {
    return cache.get(i);
  }

  @Override
  public int size() {
    return cursor.getCount();
  }
}
