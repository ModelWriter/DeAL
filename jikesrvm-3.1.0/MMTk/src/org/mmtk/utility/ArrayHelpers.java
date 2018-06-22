package org.mmtk.utility;

import org.mmtk.vm.VM;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.AddressArray;


/**
 * This is a helper class to handle sorting operations on AddressArrays 
 * and ObjectReferenceArrays.  If we were doing this in a nice object-
 * oriented way, we would extend AddressArray and ObjectReferenceArray.
 * But because they are "magic" types, I am not sure if we can do that.
 */
public class ArrayHelpers {

  /**
   * Given a sorted AddressArray and a target value, returns the index 
   * of the last occurrence of the value in it.
   * 
   * If the target value is not in the array, returns the index of
   * the last Address *less* than this object in the array. 
   * 
   * @param arr The AddressArray to search
   * @param arrEnd The length of the array (1+the index of the last element)
   * @param value The Address to search for
   * @return If the target value is in the array, the index of that value. If not,
   * the index of the last Address less than the target value.
   */
  @Uninterruptible
  public static int findSorted(AddressArray arr, int arrEnd, Address value) {
    
    int high = arrEnd, low = -1, mid;
    while (high - low > 1) {
      mid = (low + high)/2;
      if (arr.get(mid).toWord().GT(value.toWord())) {
        high = mid;
      } else {
        low = mid;
      }
    }

    /* if the value was found, low points to it.  if it was not found
     * low points to the last array value smaller than the target.
     */
    return low;

  }
  
  /**
   * Given a sorted AddressArray and a value to insert, inserts
   * the value at the correct place in the array to maintain
   * the sort.  Does not insert if the value is already in the array.
   * Assumes there is enough room in the array to add one element.
   * 
   * @param arr The AddressArray to insert into
   * @param arrEnd The length of the array (1+the index of the last element)
   * @param value The Address to insert
   * @return True if the value was inserted, false if it already existed
   * in the array
   */
  @Uninterruptible
  public static boolean insertSorted(AddressArray arr, int arrEnd, Address value) {
    
    // check that there is room in the array for this insertion
    if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(arrEnd < arr.length());
        
    int index = findSorted(arr, arrEnd, value);
    if ((index >= 0) && (arr.get(index) == value)) {
      return false;
    } else {
      for (int i=arrEnd-1; i>index; i--) {
        arr.set(i+1, arr.get(i));
      }
      arr.set(index+1, value);
      return true;
    }
  }
    
  
}
