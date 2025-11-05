import java.util.*;

public class ProductHeap {
    private List<Product> heap;
    private boolean isMinHeap;
    
    public ProductHeap(boolean isMinHeap) {
        this.heap = new ArrayList<>();
        this.isMinHeap = isMinHeap;
    }
    
    public void add(Product product) {
        heap.add(product);
        heapifyUp(heap.size() - 1);
    }
    
    public Product peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }
    
    public Product remove() {
        if (heap.isEmpty()) return null;
        
        Product root = heap.get(0);
        Product last = heap.remove(heap.size() - 1);
        
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return root;
    }
    
    public int size() {
        return heap.size();
    }
    
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    // Get all products in sorted order
    public List<Product> getAllSorted() {
        List<Product> sorted = new ArrayList<>();
        // Create a copy to avoid modifying the original heap
        List<Product> tempHeap = new ArrayList<>(this.heap);
        
        // Simple extraction sort (not most efficient but works for our needs)
        while (!tempHeap.isEmpty()) {
            Product minMax = findExtreme(tempHeap);
            sorted.add(minMax);
            tempHeap.remove(minMax);
        }
        
        return sorted;
    }
    
    private Product findExtreme(List<Product> products) {
        if (products.isEmpty()) return null;
        
        Product extreme = products.get(0);
        for (Product product : products) {
            if (isMinHeap) {
                if (product.getPrice() < extreme.getPrice()) {
                    extreme = product;
                }
            } else {
                if (product.getPrice() > extreme.getPrice()) {
                    extreme = product;
                }
            }
        }
        return extreme;
    }
    
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (shouldSwap(parentIndex, index)) {
                swap(parentIndex, index);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
    
    private void heapifyDown(int index) {
        while (index < heap.size()) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int extremeIndex = index;
            
            if (leftChild < heap.size() && shouldSwap(extremeIndex, leftChild)) {
                extremeIndex = leftChild;
            }
            
            if (rightChild < heap.size() && shouldSwap(extremeIndex, rightChild)) {
                extremeIndex = rightChild;
            }
            
            if (extremeIndex != index) {
                swap(index, extremeIndex);
                index = extremeIndex;
            } else {
                break;
            }
        }
    }
    
    private boolean shouldSwap(int parentIndex, int childIndex) {
        Product parent = heap.get(parentIndex);
        Product child = heap.get(childIndex);
        
        if (isMinHeap) {
            return parent.getPrice() > child.getPrice();
        } else {
            return parent.getPrice() < child.getPrice();
        }
    }
    
    private void swap(int i, int j) {
        Product temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
