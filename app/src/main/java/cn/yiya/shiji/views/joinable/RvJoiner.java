package cn.yiya.shiji.views.joinable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RvJoiner {

	public interface Joinable {

		RecyclerView.Adapter getAdapter();

		/**
		 * Returns type count for adapter received via {@link #getAdapter()}
		 */
		int getTypeCount();

		/**
		 * Gets type id by type index (from 0 inclusive to {@link #getTypeCount()} exclusive)
		 */
		int getTypeByIndex(int typeIndex);

	}

	private static final String TAG = RvJoiner.class.getName();

	private HostAdapter mHostAdapter;
	private boolean mAutoUpdate;

	//to find correspond observer for unregister
	private Map<Joinable, DataObserver> mJoinableToObserver = new HashMap<>();

	/**
	 * @param autoUpdate if true, joiner will listen for data updates in joined adapters,
	 *                   otherwise you have to call notify methods manually
	 * @param hasStableIds true if you want to use stable ids. Be careful, if true all your adapters
	 *                     should have stable ids, otherwise unpredictable behaviour can occurs.
	 *                     Note, that you can't set this in other time, because observers will be
	 *                     added to join adapter
	 */
	public RvJoiner(boolean autoUpdate, boolean hasStableIds) {
		mAutoUpdate = autoUpdate;
		mHostAdapter = new HostAdapter(hasStableIds);
	}

	/**
	 * The same as {@link #RvJoiner(boolean, boolean)} with auto update TRUE and stable ids FALSE
	 */
	public RvJoiner() {
		this(true, false);
	}

	/**
	 * The same as {@link #RvJoiner(boolean, boolean)} with auto update TRUE
	 * @param hasStableIds whether support stable ids
	 */
	public RvJoiner(boolean hasStableIds) {
		this(true, hasStableIds);
	}

	public int getJoinableCount() {
		return mHostAdapter.getJoinableCountInternal();
	}

	/**
	 *
	 * @param joinable joinable to add
	 * @param location location from [0 to {@link #getJoinableCount()}]
	 * @return false if already added
	 * @throws IllegalArgumentException if joinable is null
	 * @throws IndexOutOfBoundsException if location < 0 || location > {@link #getJoinableCount()}
	 */
	public boolean add(Joinable joinable, int location) {
		if (joinable == null) return false;
		boolean wasAdded = mHostAdapter.addJoinableInternal(joinable, location);
		if (wasAdded && mAutoUpdate) {
			try {//avoid "observer was already registered" exception
				if (mJoinableToObserver.get(joinable) == null) {//if no current observer
					mJoinableToObserver.put(joinable, new DataObserver(joinable, mHostAdapter));
				}
				joinable.getAdapter().registerAdapterDataObserver(mJoinableToObserver.get(joinable));
			} catch (IllegalStateException ex) {
				Log.d(TAG, "add: observer was already registered");
			}
		}
		return wasAdded;
	}

	/**
	 * Adds joinable to the bottom of joiner.
	 * @see #add(Joinable, int)
	 */
	public boolean add(Joinable joinable) {
		return add(joinable, getJoinableCount());
	}

	/**
	 * Removes joinable from joiner.
	 * @param joinable joinable to remove, not null
	 * @return false if joiner wasn't modified (joinable not exist or already removed)
	 */
	public boolean remove(Joinable joinable) {
		if (joinable == null) return false;
		if (mAutoUpdate) {
			try {//avoid "observer wasn't registered" exception
				joinable.getAdapter().unregisterAdapterDataObserver(
						mJoinableToObserver.get(joinable));
				//removing from map not needed
			} catch (IllegalStateException|IllegalArgumentException ex) {
				Log.d(TAG, "remove: observer not registered");
			}
		}
		return mHostAdapter.removeJoinableInternal(joinable);
	}

	/**
	 * @return adapter, which you can set to RecyclerView.
	 */
	public RecyclerView.Adapter getAdapter() {
		return mHostAdapter;
	}

	/**
	 * @param joinedPosition total joined position [0 .. item_count-1)
	 * @return object which wraps info, or null if position doesn't exist
	 * @see PositionInfo
	 */
	public PositionInfo getPositionInfo(int joinedPosition) {
		return mHostAdapter.getPositionInfoInternal(joinedPosition);
	}

	/**
	 * Class to wrap together extra item info. You can access info using public final fields.
	 * <pre>
	 *     {@link PositionInfo#joinedPosition} - total position in joiner
	 *     {@link PositionInfo#realPosition} - position in real hostAdapter which handles item
	 *     {@link PositionInfo#joinable} - joinable which handles item
	 *     {@link PositionInfo#joinedType} - total type in joiner
	 *     {@link PositionInfo#realType} - type in real hostAdapter which handles item
	 * </pre>
	 */
	public static class PositionInfo {

		public final int joinedPosition;
		public final int realPosition;
		public final Joinable joinable;
		public final int joinedType;
		public final int realType;

		private PositionInfo(int joinedPosition, int realPosition, Joinable joinable, int joinedType,
							 int realType) {
			this.joinedPosition = joinedPosition;
			this.realPosition = realPosition;
			this.joinable = joinable;
			this.joinedType = joinedType;
			this.realType = realType;
		}

	}

	/**
	 * Can be used as {@link RvJoiner} wrapper to get real position by joined position.
	 * This class appears because this methods
	 * <pre>
	 * {@link RecyclerView#getChildAdapterPosition(View)},
	 * {@link RecyclerView#getChildLayoutPosition(View)},
	 * {@link ViewHolder#getAdapterPosition()}
	 * {@link ViewHolder#getLayoutPosition()}
	 * </pre>
	 * returns JOINED position, but it's often more important know the real position (for ex.,
	 * when react on click and want to know what data from adapter you should use)
	 */
	public static class RealPositionProvider {

		private RvJoiner mRvJoiner;

		/**
		 * @param rvJoiner may be null
		 */
		public RealPositionProvider(RvJoiner rvJoiner) {
			this.mRvJoiner = rvJoiner;
		}

		/**
		 * Returns real position corresponded to this joined position.
		 * Will return the same value if no joiner were passed to constructor
		 */
		public int getRealPosition(int joinedPosition) {
			if (mRvJoiner == null) {
				return joinedPosition;
			} else {
				PositionInfo positionInfo = mRvJoiner.getPositionInfo(joinedPosition);
				return positionInfo != null ? positionInfo.realPosition : RecyclerView.NO_POSITION;
			}
		}

	}

	/*
	Actual joiner hostAdapter implementation.
	Joined position and joined type - values in new composite hostAdapter.
	Real position and real type - values in real sub adapters.
	So, to integrate it we need some mapping, but we don't actually need a maps, because
	joined types is in [0 .. total_types_count) and joined positions in [0 .. new_adapter_size).
	So, we cat use list to accomplish this.
	 */
	private static class HostAdapter extends RecyclerView.Adapter {

		private static final String TAG = HostAdapter.class.getName();

		private List<Joinable> mJoinables = new ArrayList<>();//should be unique
		private SparseArray<PositionInfo> mItemInfoCache = new SparseArray<>();
		private int mLastGeneratedJoinedTypeId = 0;

		//update on structure modifications
		private SparseIntArray mJoinedTypeToRealType = new SparseIntArray();
		private SparseArray<Joinable> mJoinedTypeToJoinable = new SparseArray<>();
		private Map<Joinable, SparseIntArray> mJoinableToRealToJoinedTypes = new HashMap<>();

		//update on every data change
		private int mCurrentItemCount = 0;
		private List<Integer> mJoinedPosToJoinedType = new ArrayList<>();
		private List<Integer> mJoinedPosToRealPos = new ArrayList<>();
		private List<Joinable> mJoinedPosToJoinable = new ArrayList<>();
		private Map<Joinable, int[]> mJoinableToJoinedPosArray = new HashMap<>();

		private HostAdapter(boolean hasStableIds) {
			setHasStableIds(hasStableIds);
			registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
				@Override
				public void onChanged() {
					postDataSetChanged();
				}

				@Override
				public void onItemRangeChanged(int positionStart, int itemCount) {
					onChanged();
				}

				@Override
				public void onItemRangeInserted(int positionStart, int itemCount) {
					onChanged();
				}

				@Override
				public void onItemRangeRemoved(int positionStart, int itemCount) {
					onChanged();
				}

				@Override
				public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
					onChanged();
				}
			});
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int joinedType) {
			return mJoinedTypeToJoinable.get(joinedType).getAdapter()
					.onCreateViewHolder(parent, mJoinedTypeToRealType.get(joinedType));
		}

		@Override
		@SuppressWarnings("unchecked")
		public void onBindViewHolder(ViewHolder holder, int joinedPosition) {
			mJoinedPosToJoinable.get(joinedPosition).getAdapter()
					.onBindViewHolder(holder, mJoinedPosToRealPos.get(joinedPosition));
		}

		@Override
		public long getItemId(int joinedPosition) {
			return mJoinedPosToJoinable.get(joinedPosition).getAdapter()
					.getItemId(mJoinedPosToRealPos.get(joinedPosition));
		}

		@Override
		public int getItemCount() {
			return mCurrentItemCount;
		}

		@Override
		public int getItemViewType(int joinedPosition) {
			return mJoinedPosToJoinedType.get(joinedPosition);
		}

		private int getJoinableCountInternal() {
			return mJoinables.size();
		}

		/**
		 * @return joined position for first element of joinable,
		 * or {@link RecyclerView#NO_POSITION} if joinable is not added
		 */
		private int getJoinableStartPosition(Joinable joinable) {
			int positionStart = 0;
			for (Joinable currentJoinable : mJoinables) {
				if (currentJoinable == joinable) return positionStart;
				positionStart += currentJoinable.getAdapter().getItemCount();
			}
			return RecyclerView.NO_POSITION;
		}

		private boolean addJoinableInternal(@NonNull Joinable joinable, int location) {
			if (!mJoinables.contains(joinable)) {
				mJoinables.add(location, joinable);
				postStructureChanged(joinable);
				int positionStart = getJoinableStartPosition(joinable);
				notifyItemRangeInserted(positionStart, joinable.getAdapter().getItemCount());
				return true;
			}
			return false;
		}

		private boolean removeJoinableInternal(@NonNull Joinable joinable) {
			//save this before removing
			int positionStart = getJoinableStartPosition(joinable);
			if (positionStart != RecyclerView.NO_POSITION) {//if exist
				mJoinables.remove(joinable);
				postStructureChanged(joinable);
				notifyItemRangeRemoved(positionStart, joinable.getAdapter().getItemCount());
				return true;
			}
			return false;
		}

		/**
		 * Should be called after any structure changing (changes in {@link #mJoinables}).
		 * Structure modifications changes data, so call notify method after using this method, or
		 * call {@link #postDataSetChanged()}
		 */
		private void postStructureChanged(Joinable diffJoinable) {
			if (mJoinables.contains(diffJoinable)) {//if was added
				SparseIntArray realToJoinedTypes = new SparseIntArray(diffJoinable.getTypeCount());
				for (int i = 0; i < diffJoinable.getTypeCount(); i++) {
					int newTypeId = mLastGeneratedJoinedTypeId++;
					mJoinedTypeToJoinable.put(newTypeId, diffJoinable);
					mJoinedTypeToRealType.put(newTypeId, diffJoinable.getTypeByIndex(i));
					realToJoinedTypes.put(diffJoinable.getTypeByIndex(i), newTypeId);
				}
				mJoinableToRealToJoinedTypes.put(diffJoinable, realToJoinedTypes);
			}
		}

		/**
		 * Should be called after any {@link #mHostAdapter} data set changing.
		 */
		private void postDataSetChanged() {
			mCurrentItemCount = 0;
			mJoinedPosToJoinedType.clear();
			mJoinedPosToRealPos.clear();
			mJoinedPosToJoinable.clear();
			mJoinableToJoinedPosArray.clear();
			for (Joinable joinable : mJoinables) {
				int[] joinedPosArray = new int[joinable.getAdapter().getItemCount()];
				for (int i = 0; i < joinable.getAdapter().getItemCount(); i++) {
					joinedPosArray[i] = mCurrentItemCount;
					mCurrentItemCount++;
					int itemRealType = joinable.getAdapter().getItemViewType(i);
					int itemJoinedType = mJoinableToRealToJoinedTypes.get(joinable).get(itemRealType);
					mJoinedPosToJoinedType.add(itemJoinedType);
					mJoinedPosToRealPos.add(i);
					mJoinedPosToJoinable.add(joinable);
				}
				mJoinableToJoinedPosArray.put(joinable, joinedPosArray);
			}
			mItemInfoCache.clear();
		}

		//return null, if position doesn't exist.
		private PositionInfo getPositionInfoInternal(int joinedPosition) {
			PositionInfo positionInfo = mItemInfoCache.get(joinedPosition);
			if (positionInfo == null) {
				try {
					positionInfo = new PositionInfo(
							joinedPosition,
							mJoinedPosToRealPos.get(joinedPosition),
							mJoinedPosToJoinable.get(joinedPosition),
							mJoinedPosToJoinedType.get(joinedPosition),
							mJoinedTypeToRealType.get(mJoinedPosToJoinedType.get(joinedPosition))
					);
					mItemInfoCache.put(joinedPosition, positionInfo);
				} catch (IndexOutOfBoundsException ex) {
					Log.e(TAG, "getPositionInfoInternal: position doesn't exist: " + joinedPosition, ex);
				}
			}
			return positionInfo;
		}

		/**
		 * @return position, or {@link RecyclerView#NO_POSITION} if position doesn't exist
		 */
		private int getJoinedPosition(int realPosition, Joinable joinable) {
			int[] joinedPosArray = mJoinableToJoinedPosArray.get(joinable);
			if (joinedPosArray != null && realPosition >= 0 && realPosition < joinedPosArray.length) {
				return mJoinableToJoinedPosArray.get(joinable)[realPosition];
			} else {
				return RecyclerView.NO_POSITION;
			}
		}

	}

	/**
	 * An observer to set in joinable adapters (needed for auto update implementation)
	 */
	private static class DataObserver extends RecyclerView.AdapterDataObserver {

		private Joinable mJoinable;
		private HostAdapter mHostAdapter;

		private DataObserver(Joinable joinable, HostAdapter hostAdapter) {
			mJoinable = joinable;
			mHostAdapter = hostAdapter;
		}

		@Override
		public void onChanged() {
			mHostAdapter.notifyDataSetChanged();//can't detect what happens with items, update all list
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			mHostAdapter.notifyItemRangeChanged(getJoinedPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			mHostAdapter.notifyItemRangeInserted(getJoinedPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			mHostAdapter.notifyItemRangeRemoved(getJoinedPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			if (itemCount == 1) {
				mHostAdapter.notifyItemMoved(getJoinedPosition(fromPosition),
						getJoinedPosition(toPosition));
			} else if (itemCount > 1) {
				onChanged();//no notifyItemRangeMoved method by now
			}
		}

		//just a wrapper to be short
		private int getJoinedPosition(int realPosition) {
			return mHostAdapter.getJoinedPosition(realPosition, mJoinable);
		}

	}

}
