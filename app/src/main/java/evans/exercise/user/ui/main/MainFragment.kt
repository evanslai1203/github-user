package evans.exercise.user.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import evans.exercise.user.R
import evans.exercise.user.data.User
import evans.exercise.user.databinding.MainFragmentBinding
import kotlinx.coroutines.launch

/**
 * Created by evans.lai on 2022/3/5
 */
class MainFragment : Fragment(), UserItemClickListener {

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory()
    }
    private lateinit var binding: MainFragmentBinding
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvUserList.apply {
            userListAdapter = UserListAdapter(this@MainFragment)
            adapter = userListAdapter
        }

        viewModel.userList.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it)
        }

        viewModel.photoEvent.observe(viewLifecycleOwner) {
            userListAdapter.attachPhoto(it.first, it.second)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getUserList()
        }
    }

    override fun onUserClicked(user: User) {
        viewModel.onUserClicked(user)
        findNavController().navigate(R.id.action_mainFragment_to_userDetailFragment)
    }

}