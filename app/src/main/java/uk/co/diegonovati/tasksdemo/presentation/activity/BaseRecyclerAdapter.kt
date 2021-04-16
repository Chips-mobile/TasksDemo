package uk.co.diegonovati.tasksdemo.presentation.activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import java.util.*

abstract class BaseRecyclerAdapter<ItemType, in CellType : View>(val context: Context, val itemTypeClass: Class<ItemType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Iterable<ItemType> where ItemType : Comparable<ItemType>, ItemType : Any {

    class ItemHolder(val cell: View) : RecyclerView.ViewHolder(cell)

    abstract fun onBindCell(cell: CellType?, item: ItemType)
    abstract fun onCreateCell(parent: ViewGroup?, viewType: Int): View

    protected val items: SortedList<ItemType> by lazy { createList() }
    protected var onItemClickDelegate: ((item: ItemType?) -> Unit)? = null

    private fun createList(): SortedList<ItemType> {
        return SortedList(itemTypeClass, object : SortedListAdapterCallback<ItemType>(this) {
            override fun compare(a: ItemType, b: ItemType): Int {
                val aComparable = a as? ItemType
                val bComparable = b as? ItemType
                if ((aComparable == null) && (bComparable == null)) return 0
                if ((aComparable == null) && (bComparable != null)) return -1
                if ((aComparable != null) && (bComparable == null)) return 1
                return aComparable!!.compareTo(bComparable!!)
            }

            override fun areContentsTheSame(oldItem: ItemType, newItem: ItemType): Boolean = compare(oldItem, newItem) == 0
            override fun areItemsTheSame(oldItem: ItemType, newItem: ItemType): Boolean = compare(oldItem, newItem) == 0
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= itemCount) return
        val cellHolder = holder as? ItemHolder
        val cell = cellHolder?.cell as? CellType
        val item = getItem(position) ?: return
        onBindCell(cell, item)
    }

    open fun getItem(position: Int): ItemType? {
        if (position >= itemCount) return null
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemHolder(onCreateCell(parent, viewType))
    }

    override fun getItemCount(): Int {
        return items.size()
    }

    override fun iterator(): Iterator<ItemType> = object : Iterator<ItemType> {
        private var currentPosition: Int = 0

        override fun hasNext(): Boolean {
            return currentPosition < itemCount
        }

        override fun next(): ItemType {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            return items[currentPosition++] as ItemType
        }
    }
}
