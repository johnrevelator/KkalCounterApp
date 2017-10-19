package inc.ak.kkalcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class Preferences {

        private final static String PREFERENCES= "preferences";


        public final static String AGE = "age";
        public final static String HEIGHT = "height";
        public final static String WEIGHT = "weight";
        public final static String SEX= "sex";
        public final static String SEARCH= "search";
        public final static String ACCOUNT= "account";
        public final static String ACCOUNT_NAME= "account_name";
        public final static String ACCOUNT_IMG= "account_img";
        public final static String BMR= "BMR";
        public final static String NEED_PROT= "needProt";
        public final static String NEED_CARB= "needCarb";
        public final static String NEED_FAT= "needFat";
        public final static String NEED_KCAL= "needKcal";
        public final static String PROT_KCAL= "protKcal";
        public final static String CARB_KCAL= "carbKcal";
        public final static String ACTIVE= "active";
        public final static String FAT_KCAL= "fatKcal";
        public final static String USER_ID= "user_id";






        private static SharedPreferences mSharedPreferences;

        public static void init(Context _context){
            mSharedPreferences = _context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        }
        public static void addString(String _key,String _value){
                mSharedPreferences
                        .edit()
                        .putString(_key, _value)
                        .apply();
        }
        public static String getString(String _key){
                return mSharedPreferences.getString(_key, "");
        }





}
