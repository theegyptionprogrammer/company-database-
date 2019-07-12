# company-database-
today i am going to learn how to build a simple database to add , search , delete data in android studio 
technologies which i use is : sqlite database , recyclerview as a list to show elements of the database 

what is sqlite and recyclerview?

SQLITE database 
SQLite is a relational database management system contained in a C library. In contrast to many other database management systems, SQLite is not a client–server database engine. Rather, it is embedded into the end program.

RecyclerView 
RecyclerView is flexible and efficient version of ListView. It is an container for rendering larger data set of views that can be recycled and scrolled very efficiently. RecyclerView is like traditional ListView widget, but with more flexibility to customizes and optimized to work with larger datasets. It uses a subclass of RecyclerView.Adapter for providing views that represent items in a data set.

first step in our project is to build data model , i want to build a database for a company so my module will be a Employee

class Employee {

    var name: String = ""
    var address: String = ""
    var position: String = ""
    var id: Int = 0
    var url : String = ""

    constructor(name : String , address : String , position : String , url : String , id : Int){
        this.name = name
        this.address = address
        this.position = position
        this.url = url
        this.id = id
    }
 }
 
 second step i am going to build adapter for my recyclerview 
 
 class
RecyclerViewAdapter(val employeeList: ArrayList<Employee>, val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() , Filterable {
  
    internal var searchEmployeeList : ArrayList<Employee>

    init {
        searchEmployeeList = employeeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.employee, parent, false))
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TvName?.text = employeeList[position].name
        holder.TvAddress?.text = employeeList[position].address
        holder.TvPosition?.text = employeeList[position].position
        holder.TvId?.text = employeeList[position].id.toString()

        holder.TvName?.text = searchEmployeeList[position].name
        holder.TvAddress?.text = searchEmployeeList[position].address
        holder.TvPosition?.text = searchEmployeeList[position].position
        holder.TvId?.text = searchEmployeeList[position].id.toString()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val TvName = view.findViewById<TextView>(R.id.NameTV)
        val TvAddress = view.findViewById<TextView>(R.id.AddressTV)
        val TvPosition = view.findViewById<TextView>(R.id.PositionTV)
        val TvId = view.findViewById<TextView>(R.id.IdTV)
    }

    init { this.searchEmployeeList = employeeList }

    fun removeItem(position: Int){
        employeeList.removeAt(position)
        notifyItemRemoved(position)
    }
}

now we will need to build some function to add , search and delete so will build database handler

class SqDBHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "KIRA_DB"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "entry"
        val COLUMN_NAME = "name"
        val COLUMN_ADDRESS = "address"
        val COLUMN_POSITION = "position"
        val COLUMN_ID = "id"
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${COLUMN_NAME} TEXT," +
                "${COLUMN_ADDRESS} TEXT," +
                "${COLUMN_POSITION} TEXT," +
                "${COLUMN_ID} INTEGER )"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    var db: SqDBHandler? = null


    fun AddEmployee(employee: Employee){

        val dbHandler = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.name)
            put(COLUMN_ADDRESS, employee.address)
            put(COLUMN_POSITION, employee.position)
            put(COLUMN_ID, employee.id)
        }
        dbHandler?.insert(TABLE_NAME, null, values)
        dbHandler?.close()

    }

    @SuppressLint("Recycle")
    fun GetAllEmployees(handler: SqDBHandler): ArrayList<Employee> {
        val listEmployees = ArrayList<Employee>()
        db = handler
        val dbHandler = db?.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = dbHandler?.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
                    val employee = Employee(name , address , position , id)
                    listEmployees.add(employee)
                }
            }
        }
        dbHandler?.close()
        return listEmployees
    }

    @SuppressLint("Recycle")
    fun searchEmployee(searchWord : String): ArrayList<Employee>{
        val ListEmployee = ArrayList<Employee>()
        val dbHandler = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME = $searchWord"
        var cursor = dbHandler!!.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toInt()
                    ListEmployee.add(Employee(name , address , position , id))
                    cursor.close()
                }
            }
        }
        dbHandler.close()
        return ListEmployee
    }

    fun deleteAllEmployees() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}

great now all of what we want to do is to use these function in the mainActivity and connect it to the recyclerview 
so i will start with function add Employee 

class AddEmployeeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        DoneBtn.setOnClickListener {
            save()
        }
    }

    fun save() {
        val helper = SqDBHandler(this, null)

        if (NameTE.text.isEmpty() && PositionTE.text.isEmpty() && AddressTE.text.isEmpty() && IdTE.text.isEmpty()) {
            Toast.makeText(this, "fill all the blancks ", Toast.LENGTH_SHORT).show()
        } else {
            val name = NameTE.text.toString()
            val position = PositionTE.text.toString()
            val address = AddressTE.text.toString()
            val id = Integer.parseInt(IdTE.text.toString())
            val employee = Employee(name , position , address , id)
            helper.AddEmployee(employee)
            Toast.makeText(this, "the Employee added", Toast.LENGTH_SHORT).show()
            NameTE.setText("")
            PositionTE.setText("")
            AddressTE.setText("")
            IdTE.setText("")
        }
    }
}

second function is to import all data from database and retrive it in recyclerview 

class AllEmployeesActivity : AppCompatActivity() {

    private var helper : SqDBHandler ?= null
    private var adapter : RecyclerViewAdapter ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_employees)
        UpdateBtn.setOnClickListener { update() }
        DeleteBtn.setOnClickListener{ clearAll()
        UpdateBtn.performClick()}

        val swipeHandler = object : SwipeToDeleteCallBack(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter = recyclerView.adapter as RecyclerViewAdapter
                adapter!!.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun update() {
        helper = SqDBHandler(this, null)
        val customerlist = helper!!.GetAllEmployees(helper!!)
        adapter = RecyclerViewAdapter(customerlist, this)
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter
    }

    fun clearAll(){
        val helper = SqDBHandler(this, null)
        helper.deleteAllEmployees()
    }

}

in delete function i used wipe function to delete so i build a class to control recyclerview adapter to delete an employee when we swipe it 

class SwipeToDeleteCallBack(context : Context) : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context , R.drawable.ic_delete_black_24dp)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt() , itemView.top , itemView.right , itemView.bottom)
        background.draw(c)

        val deleteIconTop = itemView.top +( itemHeight - intrinsicHeight ) / 2
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        val deleteIconMargain = ( itemHeight - intrinsicHeight ) / 2
        val deleteIconLeft = itemView.right - deleteIconMargain - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargain

        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

and i added this code in recyclerview adapter 

 init { this.searchEmployeeList = employeeList }

    fun removeItem(position: Int){
        employeeList.removeAt(position)
        notifyItemRemoved(position)
    }

and finally search function for search function there are two senarios you can choose any one as you like 
the first one to search by search function in sqlite handler , how does it work : 
you enter a name and press a button and show the employee which you searched in recyclerview if it is exist 
and here is the code 

 fun searchEmployee(searchWord : String): ArrayList<Employee>{
        val ListEmployee = ArrayList<Employee>()
        val dbHandler = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME = $searchWord"
        var cursor = dbHandler!!.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val position = cursor.getString(cursor.getColumnIndex(COLUMN_POSITION))
                    val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))).toInt()
                    ListEmployee.add(Employee(name , address , position , id))
                    cursor.close()
                }
            }
        }
        dbHandler.close()
        return ListEmployee
    }
  
the second one and which i use in my project is to show dynamicly the employees in recyclerview in same moment when we write it 
here is the code in recyclerview adapter class 

   override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    searchEmployeeList = employeeList
                } else {
                    val filteredList = ArrayList<Employee>()
                    for (employee in employeeList) {
                        if (employee.name.toLowerCase().contains(charString.toLowerCase())
                            || employee.address.toLowerCase().contains(charString.toLowerCase())
                            || employee.position.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(employee)
                        }
                            searchEmployeeList = employeeList
                    }
                }
                val filteredResults = FilterResults()
                filteredResults.values = searchEmployeeList
                return filteredResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchEmployeeList = results?.values as ArrayList<Employee>
                notifyDataSetChanged()
            }
        }
    }

    interface ClickListner{
        fun onClickListner(employee: Employee)
    }
    
 i hope you find my file useful , i accept any questions  good luch to everyone

