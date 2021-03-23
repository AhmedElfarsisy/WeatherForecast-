package com.iti.elfarsisy.mad41.myapplication.data.source.local

import android.content.Context
import androidx.room.*
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.source.local.dao.SavedPlacesDao
import com.iti.elfarsisy.mad41.myapplication.data.source.local.dao.FavoritePlacesDao
import com.iti.elfarsisy.mad41.myapplication.data.source.local.dao.WeatherDao

//@TypeConverters(DailyConverter::class, MinutelyConverter::class, HourlyConverter::class)
//, WeatherData::class
@TypeConverters(DTOConverter::class)
@Database(entities = [WeatherData::class,SavedPlaces::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun alertPlacesDao(): SavedPlacesDao
    abstract fun favoritePlacesDao(): FavoritePlacesDao
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        open fun getInstance(context: Context): WeatherDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java, "weather_app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

