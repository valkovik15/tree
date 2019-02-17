
import java.io.*;
import java.util.*;


public class Solution {
    public static class BinaryTree {
        public class Node implements Comparable<Node> {
            private int data;
            private Node left;
            private Node right;
            private Node parent;

            @Override
            public int compareTo(Node node) {
                if (data > node.data)
                    return 1;
                if (data < node.data)
                    return -1;
                return 0;
            }

            @Override
            public String toString() {
                return "Node{" +
                        "data=" + data +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Node node = (Node) o;
                return data == node.data;
            }

            @Override
            public int hashCode() {
                return data;
            }

            Node(Integer data) {
                this.data = data;
                this.left = null;
                this.right = null;
                this.height = 1;
                parent = null;
            }

            private int height;

            public int getHeight() {
                return height;
            }


        }

        private Node root;

        public BinaryTree(int data) {
            root = new Node(data);

        }

        public void addIter(Node newNode, int data) {

            Node x = root;
            Node y = null;
            while (x != null) {
                y = x;
                if (data < x.data)
                    x = x.left;
                else if (data > x.data)
                    x = x.right;
                else
                    return;
            }
            if (data < (y.data)) {
                y.left = newNode;
            } else {
                y.right = newNode;
            }
            newNode.parent = y;
            newNode.height = y.height + 1;

        }

        void iterativePreorder() {
            Queue<Node> nodeStack = Collections.asLifoQueue(new ArrayDeque<Node>());
            nodeStack.add(root);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("tst.out"))) {
                while (nodeStack.isEmpty() == false) {
                    Node mynode = nodeStack.peek();
                    bw.write(Integer.toString(mynode.data));
                    bw.newLine();
                    nodeStack.remove();
                    if (mynode.right != null) {
                        nodeStack.add(mynode.right);
                    }
                    if (mynode.left != null) {
                        nodeStack.add(mynode.left);
                    }
                }
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }

        public void addIter(int data) {
            addIter(new Node(data), data);
        }

        private Node remove(Node t, Integer key) {
            if (t == null)
                return t;
            if (t.data > (key))
                t.left = remove(t.left, key);
            else if (t.data < (key))
                t.right = remove(t.right, key);
            else if (t.right == null)
                return t.left;
            else if (t.left == null)
                return t.right;
            else {
                Node m = t.left;
                while (m.right != null)
                    m = m.right;
                t.data = m.data;
                t.left = remove(t.left, t.data);
            }
            return t;
        }

        public void remove(Integer key) {
            root = remove(root, key);
        }

        public Set<Integer> BFS() {

            LinkedList<Node> queue = new LinkedList<>();
            if (root.left != null)
                queue.add(root.left);
            if (root.right != null)
                queue.add(root.right);
            Node curNode = null;
            Set<Integer> toDel = new TreeSet<>();
            while (queue.size() != 0) {
                curNode = queue.peek();
                if (curNode.left == null && curNode.right == null) {
                    break;
                }
                if (curNode.left != null) {
                    queue.add(curNode.left);
                }
                if (curNode.right != null) {
                    queue.add(curNode.right);
                }
                queue.poll();
            }
            int length = curNode.height;
            if (length % 2 == 0) {
                return null;
            } else {
                while (queue.size() > 0) {
                    Node t = queue.poll();
                    if (t.height == length) {
                        Node[] wayFirst = new Node[length];
                        for (int i = 0; i < length; i++) {
                            wayFirst[i] = t;
                            t = t.parent;
                        }
                        int[] waySecond = new int[length];
                        int left = 0;
                        int right = length - 1;
                        for (int i = length - 1; i > 0; i--) {
                            if (wayFirst[i - 1] == wayFirst[i].left) {
                                waySecond[right] = wayFirst[i].data;
                                right--;
                            } else {
                                waySecond[left] = wayFirst[i].data;
                                left++;
                            }
                            if (waySecond[length / 2] != 0) {
                                toDel.add(waySecond[length / 2]);
                                break;
                            }
                        }
                        if(waySecond[length/2]==0)
                        {
                            toDel.add(wayFirst[0].data);
                        }
                    }
                }
            }
            return toDel;
        }

    }


    public static void main(String[] args) throws IOException {
        try {

            BufferedReader bi = new BufferedReader(new InputStreamReader(new FileInputStream("tst.in")));
            String line = new String();
            boolean noninit = true;
            BinaryTree bt = new BinaryTree(0);
            while ((line = bi.readLine()) != null) {
                Integer x = Integer.parseInt(line.trim());
                if (noninit) {
                    noninit = false;
                    bt = new BinaryTree(x);
                } else {
                    bt.addIter(x);
                }
            }
            Set<Integer> toDel = bt.BFS();
            if (toDel != null) {
                for (int i : toDel) {
                    bt.remove(i);
                }
            }
            bt.iterativePreorder();
        } catch (
                IOException ex) {

            System.out.println(ex.getMessage());
        }
    }


//bt.iterativePreorder();

}

