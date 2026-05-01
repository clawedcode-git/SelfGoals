package com.example.selfgoals.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.selfgoals.data.entity.Category;
import com.example.selfgoals.data.entity.Goal;
import com.example.selfgoals.data.entity.GoalDetails;
import com.example.selfgoals.data.entity.GoalWithCategory;
import com.example.selfgoals.data.entity.Milestone;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GoalDao_Impl implements GoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Goal> __insertionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __deletionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __updateAdapterOfGoal;

  public GoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoal = new EntityInsertionAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `goals` (`id`,`title`,`description`,`categoryId`,`deadline`,`reminderTime`,`isCompleted`,`isArchived`,`isPriority`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        if (entity.getDeadline() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDeadline());
        }
        if (entity.getReminderTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReminderTime());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isPriority() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `goals` SET `id` = ?,`title` = ?,`description` = ?,`categoryId` = ?,`deadline` = ?,`reminderTime` = ?,`isCompleted` = ?,`isArchived` = ?,`isPriority` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Goal entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        if (entity.getDeadline() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDeadline());
        }
        if (entity.getReminderTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReminderTime());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isArchived() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isPriority() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertGoal(final Goal goal, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGoal.insertAndReturnId(goal);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGoal(final Goal goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGoal.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGoal(final Goal goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGoal.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GoalDetails>> getAllGoalsWithDetails() {
    final String _sql = "SELECT * FROM goals ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "milestones",
        "goals"}, new Callable<List<GoalDetails>>() {
      @Override
      @NonNull
      public List<GoalDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
            final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderTime");
            final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfIsPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "isPriority");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final LongSparseArray<Category> _collectionCategory = new LongSparseArray<Category>();
            final LongSparseArray<ArrayList<Milestone>> _collectionMilestones = new LongSparseArray<ArrayList<Milestone>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionMilestones.containsKey(_tmpKey_1)) {
                _collectionMilestones.put(_tmpKey_1, new ArrayList<Milestone>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomExampleSelfgoalsDataEntityCategory(_collectionCategory);
            __fetchRelationshipmilestonesAscomExampleSelfgoalsDataEntityMilestone(_collectionMilestones);
            final List<GoalDetails> _result = new ArrayList<GoalDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final GoalDetails _item;
              final Goal _tmpGoal;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpDescription;
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final Long _tmpDeadline;
              if (_cursor.isNull(_cursorIndexOfDeadline)) {
                _tmpDeadline = null;
              } else {
                _tmpDeadline = _cursor.getLong(_cursorIndexOfDeadline);
              }
              final Long _tmpReminderTime;
              if (_cursor.isNull(_cursorIndexOfReminderTime)) {
                _tmpReminderTime = null;
              } else {
                _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
              }
              final boolean _tmpIsCompleted;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
              _tmpIsCompleted = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final boolean _tmpIsPriority;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsPriority);
              _tmpIsPriority = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpGoal = new Goal(_tmpId,_tmpTitle,_tmpDescription,_tmpCategoryId,_tmpDeadline,_tmpReminderTime,_tmpIsCompleted,_tmpIsArchived,_tmpIsPriority,_tmpCreatedAt);
              final Category _tmpCategory;
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_2 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_2);
              } else {
                _tmpCategory = null;
              }
              final ArrayList<Milestone> _tmpMilestonesCollection;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfId);
              _tmpMilestonesCollection = _collectionMilestones.get(_tmpKey_3);
              _item = new GoalDetails(_tmpGoal,_tmpCategory,_tmpMilestonesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getGoalWithCategoryById(final long id,
      final Continuation<? super GoalWithCategory> $completion) {
    final String _sql = "SELECT * FROM goals WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<GoalWithCategory>() {
      @Override
      @Nullable
      public GoalWithCategory call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
            final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderTime");
            final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
            final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
            final int _cursorIndexOfIsPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "isPriority");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final LongSparseArray<Category> _collectionCategory = new LongSparseArray<Category>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomExampleSelfgoalsDataEntityCategory(_collectionCategory);
            final GoalWithCategory _result;
            if (_cursor.moveToFirst()) {
              final Goal _tmpGoal;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpDescription;
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final Long _tmpDeadline;
              if (_cursor.isNull(_cursorIndexOfDeadline)) {
                _tmpDeadline = null;
              } else {
                _tmpDeadline = _cursor.getLong(_cursorIndexOfDeadline);
              }
              final Long _tmpReminderTime;
              if (_cursor.isNull(_cursorIndexOfReminderTime)) {
                _tmpReminderTime = null;
              } else {
                _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
              }
              final boolean _tmpIsCompleted;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
              _tmpIsCompleted = _tmp != 0;
              final boolean _tmpIsArchived;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
              _tmpIsArchived = _tmp_1 != 0;
              final boolean _tmpIsPriority;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsPriority);
              _tmpIsPriority = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpGoal = new Goal(_tmpId,_tmpTitle,_tmpDescription,_tmpCategoryId,_tmpDeadline,_tmpReminderTime,_tmpIsCompleted,_tmpIsArchived,_tmpIsPriority,_tmpCreatedAt);
              final Category _tmpCategory;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_1 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_1);
              } else {
                _tmpCategory = null;
              }
              _result = new GoalWithCategory(_tmpGoal,_tmpCategory);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Goal>> getGoalsByCategory(final long categoryId) {
    final String _sql = "SELECT * FROM goals WHERE categoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"goals"}, new Callable<List<Goal>>() {
      @Override
      @NonNull
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderTime");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfIsPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "isPriority");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final Long _tmpDeadline;
            if (_cursor.isNull(_cursorIndexOfDeadline)) {
              _tmpDeadline = null;
            } else {
              _tmpDeadline = _cursor.getLong(_cursorIndexOfDeadline);
            }
            final Long _tmpReminderTime;
            if (_cursor.isNull(_cursorIndexOfReminderTime)) {
              _tmpReminderTime = null;
            } else {
              _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            }
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final boolean _tmpIsArchived;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_1 != 0;
            final boolean _tmpIsPriority;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsPriority);
            _tmpIsPriority = _tmp_2 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Goal(_tmpId,_tmpTitle,_tmpDescription,_tmpCategoryId,_tmpDeadline,_tmpReminderTime,_tmpIsCompleted,_tmpIsArchived,_tmpIsPriority,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomExampleSelfgoalsDataEntityCategory(
      @NonNull final LongSparseArray<Category> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomExampleSelfgoalsDataEntityCategory(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`color` FROM `categories` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColor = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Category _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpColor;
          _tmpColor = _cursor.getInt(_cursorIndexOfColor);
          _item_1 = new Category(_tmpId,_tmpName,_tmpColor);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipmilestonesAscomExampleSelfgoalsDataEntityMilestone(
      @NonNull final LongSparseArray<ArrayList<Milestone>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipmilestonesAscomExampleSelfgoalsDataEntityMilestone(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`goalId`,`title`,`isCompleted`,`position` FROM `milestones` WHERE `goalId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "goalId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfGoalId = 1;
      final int _cursorIndexOfTitle = 2;
      final int _cursorIndexOfIsCompleted = 3;
      final int _cursorIndexOfPosition = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Milestone> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Milestone _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpGoalId;
          _tmpGoalId = _cursor.getLong(_cursorIndexOfGoalId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final boolean _tmpIsCompleted;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
          _tmpIsCompleted = _tmp != 0;
          final int _tmpPosition;
          _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
          _item_1 = new Milestone(_tmpId,_tmpGoalId,_tmpTitle,_tmpIsCompleted,_tmpPosition);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
