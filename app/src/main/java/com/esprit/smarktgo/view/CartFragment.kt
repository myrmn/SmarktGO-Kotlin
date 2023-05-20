package com.esprit.smarktgo.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ItemAdapter
import com.esprit.smarktgo.viewmodel.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class CartFragment(val mainActivity: MainActivity): Fragment() {

    lateinit var cartViewModel: CartViewModel
    lateinit var rv: RecyclerView
    lateinit var imageV: ImageView
    lateinit var textV: TextView
    lateinit var marker: ImageView
    lateinit var supermarketName: TextView
    lateinit var totalTV: TextView
    lateinit var priceTV: TextView
    lateinit var payCardView: CardView
    lateinit var itemAdapter: ItemAdapter
    lateinit var view2 : View
    lateinit var bottomSheetDialog : BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_cart,container,false)
        view2 = view
        rv = view.findViewById(R.id.rv_order_items)
        textV = view.findViewById(R.id.emptyCartTV)
        imageV = view.findViewById(R.id.imageView)
        marker = view.findViewById(R.id.marker)
        supermarketName = view.findViewById(R.id.supermarketName)
        totalTV = view.findViewById(R.id.totalTV)
        priceTV = view.findViewById(R.id.priceTV)
        payCardView = view.findViewById(R.id.payCardView)

        prepareRecyclerView()
        cartViewModel = CartViewModel(this)
        cartViewModel.observeOrderLiveData().observe(requireActivity(), Observer { order ->
            supermarketName.text = order.items[0].supermarketName
            priceTV.text = "%.1f".format(cartViewModel.getTotal()) + " TND"
            itemAdapter.setList(order.items)
        })

        payCardView.setOnClickListener {
            showBottomSheet()
        }

        return view
    }

    private fun prepareRecyclerView() {
        itemAdapter = ItemAdapter(null,this)
        rv.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL ,false)
        }
    }
    fun showImage(){
        imageV.isVisible = true
        textV.isVisible = true
    }

    fun showOrderInfo(visibility: Boolean)
    {
        marker.isVisible = visibility
        supermarketName.isVisible = visibility
        totalTV.isVisible = visibility
        priceTV.isVisible = visibility
        payCardView.isVisible = visibility
        rv.isVisible = visibility
    }


    private fun showBottomSheet()
    {
        bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val wallet = bottomSheetView.findViewById<TextView>(R.id.walletTV2)
        cartViewModel.showWallet(wallet)
        bottomSheetView.findViewById<TextView>(R.id.totalTV2).text = "%.1f".format(cartViewModel.getTotal()) + " TND"
        bottomSheetView.findViewById<Button>(R.id.payButton).setOnClickListener {
            cartViewModel.pay()
        }
    }

    fun showSnackBar()
    {
        bottomSheetDialog.dismiss()
        Snackbar.make(view2.findViewById(R.id.cartConstraintLayout),"Not enough money!", Snackbar.LENGTH_LONG).show()
    }


}