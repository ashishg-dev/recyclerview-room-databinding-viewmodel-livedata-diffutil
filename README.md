<p><strong>Using Room Database with kotlin.</strong></p>
<p><strong>What is Room?</strong><br />Room is a SQLite object mapping library. Use it to avoid boilerplate code and easily convert SQLite table data to Java objects. Room provides compile time checks of SQLite statements and can return RxJava, Flowable and LiveData observables.</p>
<p><strong>Why use Room?</strong><br />Compile-time verification of SQL queries. each @Query and @Entity is checked at the compile time, that preserves your app from crash issues at runtime and not only it checks the only syntax, but also missing tables.<br />Boilerplate code<br />Easily integrated with other Architecture components (like LiveData)</p>
<p><strong>What you will build?</strong><br />You will build an app that implements our <a href="https://developer.android.com/topic/libraries/architecture/guide.html">recommended architecture</a> using the Android Architecture Components. The sample app stores a list of user details in a Room database and displays it in a RecyclerView.</p>
<p><br />There are 3 major components in Room:</p>
<ul>
<li><a href="https://developer.android.com/reference/androidx/room/Database">Database</a>:&nbsp;Contains the database holder and serves as the main access point for the underlying connection to your app's persisted, relational data.&nbsp; The class that's annotated with&nbsp;@Database&nbsp;should satisfy the following conditions:<br />Be an abstract class that extends&nbsp;RoomDatabase.<br />Include the list of entities associated with the database within the annotation.<br />Contain an abstract method that has 0 arguments and returns the class that is annotated with&nbsp;@Dao.<br />At runtime, you can acquire an instance of&nbsp;Database&nbsp;by calling&nbsp;Room.databaseBuilder()&nbsp;or&nbsp;Room.inMemoryDatabaseBuilder().</li>
<li><a href="https://developer.android.com/training/data-storage/room/defining-data">Entity</a>:&nbsp;Represents a table within the database.</li>
<li><a href="https://developer.android.com/training/data-storage/room/accessing-data">DAO</a>:&nbsp;Contains the methods used for accessing the database.</li>
</ul>
<p>This relationship among the different components of the Room:</p>
<p><img src="https://developer.android.com/images/training/data-storage/room_architecture.png" alt="" width="600" height="542" /></p>
<p>&nbsp;</p>
<h1 id="f7dd" class="kn jm ct bj bi jn fq ko fs kp kq kr ks kt ku kv kw" data-selectable-paragraph="">Implementation of Room</h1>
<p>Step 1 - Add the following dependencies to your app's&nbsp;<code dir="ltr" translate="no">build.gradle</code>&nbsp;file:</p>
<pre class="clear-for-copy"><span class="pln">dependencies </span><span class="pun">{</span><span class="pln"><br />&nbsp; </span><span class="kwd">def</span><span class="pln"> room_version </span><span class="pun">=</span> <span class="str">"2.2.5"</span><span class="pln"><br /><br />&nbsp; implementation </span><span class="str">"androidx.room:room-runtime:$room_version"</span><span class="pln"><br />&nbsp; annotationProcessor </span><span class="str">"androidx.room:room-compiler:$room_version"</span> <span class="com">// For Kotlin use kapt instead of annotationProcessor</span><span class="pln"><br /><br />&nbsp; </span><span class="com">// optional - Kotlin Extensions and Coroutines support for Room</span><span class="pln"><br />&nbsp; implementation </span><span class="str">"androidx.room:room-ktx:$room_version"</span><span class="pln"><br /><br />&nbsp; </span><span class="com">// optional - RxJava support for Room</span><span class="pln"><br />&nbsp; implementation </span><span class="str">"androidx.room:room-rxjava2:$room_version"</span><span class="pln"><br /><br />&nbsp; </span><span class="com">// optional - Guava support for Room, including Optional and ListenableFuture</span><span class="pln"><br />&nbsp; implementation </span><span class="str">"androidx.room:room-guava:$room_version"</span><span class="pln"><br /><br />&nbsp; </span><span class="com">// Test helpers</span><span class="pln"><br />&nbsp; testImplementation </span><span class="str">"androidx.room:room-testing:$room_version"</span><span class="pln"><br /></span><span class="pun">}<br /></span></pre>
<p>&nbsp;</p>

<p class="step-title">Step 2 - <a href="https://github.com/ashishgupta191193/recyclerview-room-databinding-viewmodel-livedata-diffutil/blob/master/app/src/main/java/dev/ashish/roomdatabase/entity/UserDetails.kt">Create an Entity</a></p>
<pre>@Entity(tableName = "user_details")
data class UserDetails(
    @PrimaryKey @ColumnInfo(name = "userId") val userId : Int,
    @ColumnInfo(name = "firstName") val firstName : String,
    @ColumnInfo(name = "lastName") val lastName : String,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "mobile_number") val mobileNumber : String
)
</pre>
<p>Step 3 - <a href="https://github.com/ashishgupta191193/recyclerview-room-databinding-viewmodel-livedata-diffutil/blob/master/app/src/main/java/dev/ashish/roomdatabase/dao/UserDetailsDao.kt">Create the DAO</a></p>
<p id="0884" class="ii iw ct bj ik b il jz ix in ka iy ip kb iz ir kc ja it kd jb iv fh" data-selectable-paragraph=""><a class="cg dj ke kf kg kh" href="https://developer.android.com/topic/libraries/architecture/room.html#daos" target="_blank" rel="noopener nofollow">DAOs</a>&nbsp;are responsible for defining the methods that access the database.</p>
<p id="53c3" class="ii iw ct bj ik b il im ix in io iy ip iq iz ir is ja it iu jb iv fh" data-selectable-paragraph="">To create a DAO we need to create an interface and annotated with @Dao.</p>
<pre>
@Dao
interface UserDetailsDao {

    @Query("SELECT * from user_details")
    fun getAlphabetizedDetails(): LiveData&lt;List&gt;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userDetails: UserDetails)

    @Delete
    suspend fun delete(userDetails: UserDetails)
    
    @Update
    suspend fun update(userDetails: UserDetails)

}
</pre>
<br/>
<p>Step 4 - Add a Room Database</p>
<pre>@Database(entities = [UserDetails::class], version = 1, exportSchema = false)
public abstract class MyDatabase : RoomDatabase() {

    abstract fun userDetailsDao(): UserDetailsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MyDatabase? = null

         fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    MyDatabase::class.java, "example_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
</pre>
