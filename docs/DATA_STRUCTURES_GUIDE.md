# Custom Data Structures Reference Guide

A quick reference for every custom data structure in this project. Each structure is built entirely from scratch — no `java.util` collections used.

---

## 1. CustomDynamicArray\<T\>
**File:** `datastructures/CustomDynamicArray.java`  
**Purpose:** Replaces `java.util.ArrayList`. A resizable array that doubles in capacity when full.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `get(index)` | O(1) | Direct array access |
| `set(index, element)` | O(1) | Direct array update |
| `insert(element)` | O(1) amortized | Appends to end; resizes if full |
| `insertAt(index, element)` | O(n) | Shifts elements right |
| `remove(index)` | O(n) | Shifts elements left |
| `indexOf(element)` | O(n) | Linear scan |
| `size()` | O(1) | Returns count |

**Used in:** Everywhere — holds lists of locations, roads, requests, resources, and algorithm results.

**Index parameter:** Initial capacity is derived from team index number.

---

## 2. CustomSinglyLinkedList\<T\>
**File:** `datastructures/CustomSinglyLinkedList.java`  
**Purpose:** Node-based list where each node points to the next. Used for graph adjacency lists and hash table collision chains.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `addFirst(element)` | O(1) | Inserts at head |
| `addLast(element)` | O(n) | Traverses to tail, appends |
| `insertAfter(node, element)` | O(1) | Inserts after given node |
| `remove(element)` | O(n) | Finds and unlinks node |
| `get(index)` | O(n) | Traverses to index |
| `iterator()` | O(1) | Returns iterable traversal |

---

## 3. CustomDoublyLinkedList\<T\>
**File:** `datastructures/CustomDoublyLinkedList.java`  
**Purpose:** Each node has prev and next pointers, allowing bidirectional traversal.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `addFirst(element)` | O(1) | Inserts at head |
| `addLast(element)` | O(1) | Inserts at tail (tail pointer) |
| `removeFirst()` | O(1) | Removes head |
| `removeLast()` | O(1) | Removes tail |
| `insertBefore(node, element)` | O(1) | Inserts before given node |

---

## 4. CustomStack\<T\>
**File:** `datastructures/CustomStack.java`  
**Purpose:** Last-In-First-Out (LIFO) container. Used for undo/audit trail, DFS traversal, and recursion simulation.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `push(element)` | O(1) | Adds to top |
| `pop()` | O(1) | Removes and returns top |
| `peek()` | O(1) | Views top without removing |
| `isEmpty()` | O(1) | Checks if empty |

**Where it is used:**
- `DispatchEngine` — stores cancelled requests for undo
- `GraphAlgorithms` — iterative DFS uses stack instead of recursion

---

## 5. CustomQueue\<T\>
**File:** `datastructures/CustomQueue.java`  
**Purpose:** First-In-First-Out (FIFO) queue. Basic array-backed implementation.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `enqueue(element)` | O(1) | Adds to rear |
| `dequeue()` | O(1) | Removes from front |
| `peek()` | O(1) | Views front element |

---

## 6. CustomCircularQueue\<T\>
**File:** `datastructures/CustomCircularQueue.java`  
**Purpose:** Array-backed queue where the rear wraps around to the front, preventing wasted space. Used for FIFO dispatch.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `enqueue(element)` | O(1) | Inserts at `(rear + 1) % capacity` |
| `dequeue()` | O(1) | Removes at `front`, advances `front = (front + 1) % capacity` |

**Trace output:** Prints front and rear pointer positions after each operation.

---

## 7. CustomDeque\<T\>
**File:** `datastructures/CustomDeque.java`  
**Purpose:** Double-ended queue allowing insertion and removal from both ends. Used for urgent request insertion.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `addFront(element)` | O(1) | Inserts at front |
| `addRear(element)` | O(1) | Inserts at rear |
| `removeFront()` | O(1) | Removes from front |
| `removeRear()` | O(1) | Removes from rear |

---

## 8. CustomMaxHeap
**File:** `datastructures/CustomMaxHeap.java`  
**Purpose:** Binary max-heap used as a priority queue. The highest-priority request is always at the root. Used for priority-based dispatch.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `insert(element)` | O(log n) | Adds element, bubbles up |
| `extractMax()` | O(log n) | Removes root, bubbles down |
| `peek()` | O(1) | Views maximum element |
| `heapifyUp(index)` | O(log n) | Restores heap property upward |
| `heapifyDown(index)` | O(log n) | Restores heap property downward |

**Trace output:** Prints array swaps during heapify operations (e.g., "SWAP: index 3 ↔ index 1").

---

## 9. CustomMinHeap
**File:** `datastructures/CustomMinHeap.java`  
**Purpose:** Binary min-heap used in Dijkstra's algorithm. The node with smallest distance is always at the root.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `insert(element)` | O(log n) | Adds element, bubbles up |
| `extractMin()` | O(log n) | Removes root, bubbles down |
| `decreaseKey(element, newKey)` | O(log n) | Decreases key value, bubbles up |
| `contains(element)` | O(n) or O(1) with index map | Checks membership |

---

## 10. CustomBST\<T\>
**File:** `datastructures/CustomBST.java`  
**Purpose:** Binary Search Tree for ordered data. Used for indexing requests by timestamp and campus zone.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `insert(element)` | O(h) | Inserts maintaining BST property |
| `search(key)` | O(h) | Follows left/right based on comparison |
| `delete(key)` | O(h) | Handles 0, 1, or 2 children cases |
| `inorderTraversal()` | O(n) | Returns sorted order |
| `min()`, `max()` | O(h) | Leftmost / rightmost node |
| `height()` | O(n) | Computes tree height |

*h = tree height (O(log n) if balanced, O(n) worst case)*

---

## 11. CustomRedBlackTree\<T\>
**File:** `datastructures/CustomRedBlackTree.java`  
**Purpose:** Self-balancing BST guaranteeing O(log n) operations. Maintains balance through node coloring and rotations.

**Red-Black properties:**
1. Every node is either RED or BLACK
2. Root is always BLACK
3. No two consecutive RED nodes (red node's children are black)
4. Every path from root to null has the same number of BLACK nodes

**Key operations:**
| Method | Description |
|--------|-------------|
| `insert(element)` | Inserts then calls fixup to rebalance |
| `leftRotate(node)` | Rotates subtree left |
| `rightRotate(node)` | Rotates subtree right |
| `fixupAfterInsert(node)` | Recolors and rotates to restore properties |

---

## 12. CustomBTree
**File:** `datastructures/CustomBTree.java`  
**Purpose:** Multi-way search tree simulating database index pages. Nodes hold multiple keys and split when full.

**Key operations:**
| Method | Description |
|--------|-------------|
| `insert(key)` | Inserts key, splits node if it exceeds order |
| `search(key)` | Traverses tree following key comparisons |
| `splitChild(parent, index)` | Splits full child node into two |

---

## 13. CustomHashTable\<K, V\>
**File:** `datastructures/CustomHashTable.java`  
**Purpose:** O(1) average-time key-value lookups using chaining (linked list buckets) for collision resolution. Used for instant request lookup by ID.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `put(key, value)` | O(1) average | Inserts or updates entry |
| `get(key)` | O(1) average | Retrieves value by key |
| `remove(key)` | O(1) average | Removes entry |
| `containsKey(key)` | O(1) average | Checks if key exists |
| `loadFactor()` | O(1) | Returns size / capacity |
| `rehash()` | O(n) | Doubles capacity when load > 0.75 |

**Index parameter:** Table capacity `M` is derived from team index number.

**Collision tracking:** Reports number of collisions, longest chain, and load factor statistics.

---

## 14. CustomDisjointSet
**File:** `datastructures/CustomDisjointSet.java`  
**Purpose:** Union-Find structure for tracking connected components. Used in Kruskal's MST algorithm.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `makeSet(element)` | O(1) | Creates singleton set |
| `find(element)` | O(α(n)) ≈ O(1) | Finds set representative (with path compression) |
| `union(a, b)` | O(α(n)) ≈ O(1) | Merges two sets (by rank) |

---

## 15. CustomSkipList\<T\>
**File:** `datastructures/CustomSkipList.java`  
**Purpose:** Probabilistic data structure providing O(log n) average search, insert, and delete.

**Key operations:**
| Method | Time Complexity | Description |
|--------|----------------|-------------|
| `insert(element)` | O(log n) avg | Inserts with random level |
| `search(element)` | O(log n) avg | Searches across levels |
| `delete(element)` | O(log n) avg | Removes and adjusts levels |
| `displayLevels()` | O(n) | Shows all skip list levels |

---

## 16. CustomSet\<T\>
**File:** `datastructures/CustomSet.java`  
**Purpose:** Set abstraction built on top of `CustomHashTable`. Ensures uniqueness.

**Key operations:** `add`, `remove`, `contains`, `size`, `union`, `intersection`

---

## 17. CustomMap\<K, V\>
**File:** `datastructures/CustomMap.java`  
**Purpose:** Key-value map built on top of `CustomHashTable`.

**Key operations:** `put`, `get`, `remove`, `containsKey`, `keys`, `values`, `entrySet`
