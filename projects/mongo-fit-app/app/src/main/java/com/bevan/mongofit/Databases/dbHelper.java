package com.bevan.mongofit.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    // Creating the database
    public static final String DATABASE_NAME = "MongoFit";

    //*************************************************************************************
    // Table Users
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_USERS = "tblUsers";
    // Creating the columns
    //public static final String USER_ID = "fldUserID";
    public static final String USERNAME = "fldUsername";
    public static final String PASSWORD = "fldPassword";
    public static final String AGE = "fldAge";
    public static final String GENDER = "fldGender";

    //SQL Creating the table
    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + USERNAME + " TEXT PRIMARY KEY, "
            + AGE + " TEXT, "
            + GENDER + " TEXT, "
            + PASSWORD + " TEXT);";

    //*************************************************************************************
    // Table User Info
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_INFO = "tblInfo";
    // Creating the columns
    public static final String INFO_ID = "fldInfoID";
    public static final String WEIGHT = "fldWeight";
    public static final String GOAL_WEIGHT = "fldGoalWeight";
    public static final String HEIGHT = "fldHeight";
    public static final String SYSTEM = "fldSystem";

    //SQL Creating the table
    public static final String CREATE_TABLE_INFO = "CREATE TABLE " + TABLE_INFO + " ("
            + INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + WEIGHT + " TEXT, "
            + GOAL_WEIGHT + " TEXT,"
            + HEIGHT + " TEXT, "
            + SYSTEM + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";

    //*************************************************************************************
    // Table Steps
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_STEPS = "tblSteps";
    // Creating the columns
    public static final String STEP_ID = "fldStepID";
    public static final String STEP_MAX = "fldStepsMax";

    //SQL Creating the table
    public static final String CREATE_TABLE_STEPS = "CREATE TABLE " + TABLE_STEPS + " ("
            + STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STEP_MAX + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";


    //*************************************************************************************
    // Table Steps History
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_STEPS_HISTORY = "tblStepsHistory";
    // Creating the columns
    public static final String STEP_HISTORY_ID = "fldStepHistoryID";
    public static final String STEPS = "fldSteps";
    public static final String DISTANCE = "fldDistance";
    public static final String CALORIES_BURNT = "fldCaloriesBurnt";

    //SQL Creating the table
    public static final String CREATE_TABLE_STEPS_HISTORY = "CREATE TABLE " + TABLE_STEPS_HISTORY + " ("
            + STEP_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STEPS + " TEXT, "
            + DISTANCE + " TEXT, "
            + CALORIES_BURNT + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";

    //*************************************************************************************
    // Table Calories
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_CALORIES = "tblCalories";
    // Creating the columns
    public static final String CALORIE_ID = "fldCalorieID";
    public static final String CALORIE_MAX = "fldCalorieMax";

    public static final String CREATE_TABLE_CALORIE = "CREATE TABLE " + TABLE_CALORIES + " ("
            + CALORIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CALORIE_MAX + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";

    //*************************************************************************************
    // Table Calories History
    //*************************************************************************************
    // Creating the users table
    public static final String TABLE_CALORIES_HISTORY = "tblCaloriesHistory";
    // Creating the columns
    public static final String CALORIE_HISTORY_ID = "fldCalorieHistoryID";
    public static final String CALORIE_CONSUMED = "fldCalorieConsumed";
    public static final String CALORIE_REMAIN = "fldCalorieRemain";

    public static final String CREATE_TABLE_CALORIE_HISTORY = "CREATE TABLE " + TABLE_CALORIES_HISTORY + " ("
            + CALORIE_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CALORIE_CONSUMED + " TEXT, "
            + CALORIE_REMAIN + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";

    //*************************************************************************************
    // Table Weight History
    //*************************************************************************************

    // Creating the users table
    public static final String TABLE_WEIGHT_HISTORY = "tblWeightHistory";
    // Creating the columns
    public static final String WEIGHT_HISTORY_ID = "fldWeightHistoryID";
    public static final String WEIGHT_DAY = "fldWeightDay";
    public static final String WEIGHT_WEIGHT = "fldWeight";

    public static final String CREATE_TABLE_WEIGHT_HISTORY = "CREATE TABLE " + TABLE_WEIGHT_HISTORY + " ("
            + WEIGHT_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + WEIGHT_DAY + " TEXT, "
            + WEIGHT_WEIGHT + " TEXT, "
            + " fldUsername TEXT,"
            + " FOREIGN KEY(fldUsername) REFERENCES tblUsers (fldUsername));";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_INFO);
        sqLiteDatabase.execSQL(CREATE_TABLE_STEPS);
        sqLiteDatabase.execSQL(CREATE_TABLE_STEPS_HISTORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_CALORIE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CALORIE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_WEIGHT_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS_HISTORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES_HISTORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_HISTORY);
        onCreate(sqLiteDatabase);
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR USER INFO
    //*************************************************************************************

    // method check to see if username and password exists in the BD
    public boolean checkUserAndPassword(String username, String password) {
        // create an instance of the SQL DB
        SQLiteDatabase db = this.getWritableDatabase();

        //surround it with a try catch
        try {
            // cursor class
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE fldUsername =? AND fldPassword =? ", new String[]{username, password});

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // Registering a user
    public boolean RegisteringUser(String username, String age, String gender, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(AGE, age);
        contentValues.put(GENDER, gender);
        //db.insert(TABLE_USERS,null ,contentValues);
        long result = db.insert(TABLE_USERS, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Registering the users weight and height
    public boolean RegisteringUserInfo(String height, String weight, String goalWeight, String username, String sys) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT, weight);
        contentValues.put(GOAL_WEIGHT, goalWeight);
        contentValues.put(HEIGHT, height);
        contentValues.put(SYSTEM, sys);
        contentValues.put(USERNAME, username);
        //db.insert(TABLE_USERS, null, contentValues);
        long result = db.insert(TABLE_INFO, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Checking if the user exists in the database
    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Deleting an account
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + TABLE_USERS + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR GETTING THE INFO
    //*************************************************************************************

    // getting the password for the profile
    public String[] getPassword(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldPassword FROM " + TABLE_USERS + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
            }
        }
        return details;
    }

    // getting the age and gender for the profile
    public String[] getAgeAndGender(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldAge, fldGender FROM " + TABLE_USERS + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
                details[1] = cursor.getString(1);
            }
        }
        return details;
    }

    // getting the weight and height for the profile
    public String[] getWeightAndHeight(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldWeight, fldGoalWeight, fldHeight, fldSystem FROM " + TABLE_INFO + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
                details[1] = cursor.getString(1);
                details[2] = cursor.getString(2);
                details[3] = cursor.getString(3);
            }
        }
        return details;
    }

    // updating the info in the profile
    public boolean updateInfo(String username, String age, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSWORD, password);
        contentValues.put(AGE, age);
        long result = db.update(TABLE_USERS, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // updating the weight, height and goal weight in the profile
    public boolean updateWeightAndHeight(String username, String weight, String goalWeight, String height) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT, weight);
        contentValues.put(GOAL_WEIGHT, goalWeight);
        contentValues.put(HEIGHT, height);
        long result = db.update(TABLE_INFO, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR CHECKING THE STEPS AND CALORIES ARE IN THE DATABASE
    //*************************************************************************************

    // checking if the database has the steps already in it with that user to track the steps
    public boolean checkUserInSteps(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STEPS + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUserInStepsHistory(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STEPS_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUserInCalories(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CALORIES + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUserInCalorieHistory(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CALORIES_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUserInWeightHistory(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEIGHT_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR GETTING THE STEPS AND CALORIES
    //*************************************************************************************

    public String[] getSteps(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldStepsMax FROM " + TABLE_STEPS + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
            }
        }
        return details;
    }

    public String[] getCalories(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldCalorieMax FROM " + TABLE_CALORIES + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
            }
        }
        return details;
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR INSERT THE STEPS AND CALORIES
    //*************************************************************************************

    public boolean insertSteps(String username, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STEP_MAX, steps);
        contentValues.put(USERNAME, username);
        long result = db.insert(TABLE_STEPS, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertCalories(String username, String calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CALORIE_MAX, calories);
        contentValues.put(USERNAME, username);
        long result = db.insert(TABLE_CALORIES, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR UPDATING THE STEPS AND CALORIES
    //*************************************************************************************

    public boolean updateSteps(String steps, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STEP_MAX, steps);
        long result = db.update(TABLE_STEPS, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateCalories(String calories, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CALORIE_MAX, calories);
        long result = db.update(TABLE_CALORIES, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR GETTING STEP AND CALORIES HISTORY
    //*************************************************************************************

    public String[] getStepHistory(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldSteps, fldDistance, fldCaloriesBurnt FROM " + TABLE_STEPS_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
                details[1] = cursor.getString(1);
                details[2] = cursor.getString(2);
            }
        }
        return details;
    }

    public String[] getCalorieHistory(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldCalorieConsumed, fldCalorieRemain FROM " + TABLE_CALORIES_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
                details[1] = cursor.getString(1);
            }
        }
        return details;
    }

    public String[] getWeightHistory(String username, String day) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT fldWeightDay, fldWeight FROM " + TABLE_WEIGHT_HISTORY + " WHERE fldUsername=? AND fldWeightDay=?", new String[]{username, day});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
                details[1] = cursor.getString(1);
            }
        }
        return details;
    }

    public String[] getWeightHistoryMax(String username) {
        String[] details = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(fldWeightDay) FROM " + TABLE_WEIGHT_HISTORY + " WHERE fldUsername=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                details = new String[cursor.getColumnCount()];
                details[0] = cursor.getString(0);
            }
        }
        return details;
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR INSERT STEP AND CALORIES HISTORY
    //*************************************************************************************

    public boolean insertStepsHistory(String username, String steps, String distance, String calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS, steps);
        contentValues.put(DISTANCE, distance);
        contentValues.put(CALORIES_BURNT, calories);
        contentValues.put(USERNAME, username);
        long result = db.insert(TABLE_STEPS_HISTORY, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertCalorieHistory(String username, String consumed, String remain) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CALORIE_CONSUMED, consumed);
        contentValues.put(CALORIE_REMAIN, remain);
        contentValues.put(USERNAME, username);
        long result = db.insert(TABLE_CALORIES_HISTORY, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertWeightHistory(String username, String day, String weight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIGHT_DAY, day);
        contentValues.put(WEIGHT_WEIGHT, weight);
        contentValues.put(USERNAME, username);
        long result = db.insert(TABLE_WEIGHT_HISTORY, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //*************************************************************************************
    // SQL STATEMENTS FOR UPDATING STEP AND CALORIES HISTORY
    //*************************************************************************************

    public boolean updateStepsHistory(String username, String steps, String distance, String calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STEPS, steps);
        contentValues.put(DISTANCE, distance);
        contentValues.put(CALORIES_BURNT, calories);
        long result = db.update(TABLE_STEPS_HISTORY, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateCalorieHistory(String username, String consumed, String remain) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CALORIE_CONSUMED, consumed);
        contentValues.put(CALORIE_REMAIN, remain);
        contentValues.put(USERNAME, username);
        long result = db.update(TABLE_CALORIES_HISTORY, contentValues, "fldUsername=?", new String[]{username});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
