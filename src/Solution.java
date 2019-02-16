
import java.io.*;
import java.util.*;




public class Solution {
    public static class BinaryTree {
        public class Node implements Comparable<Node> {
            private int data;
            private Node left;
            private Node right;

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
            }

            private int height;

            public int getHeight() {
                return height;
            }


        }

        private Node root;

        public static Set<Node> leaves;
        public static HashMap<Node, ArrayList<Integer>> waysTo;

        static {
            waysTo = new HashMap<>(500);
            leaves = new TreeSet<>();
        }


        public BinaryTree(int data) {
            root = new Node(data);
            waysTo.clear();
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(root.data);
            waysTo.put(root, temp);
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
            ArrayList<Integer> temp = new ArrayList<>(waysTo.get(y));
            if (data < (y.data)) {
                temp.add(temp.lastIndexOf(y.data), data);
                y.left = newNode;

            } else {
                int index = temp.indexOf(y.data) + 1;
                try {
                    temp.add(index, data);
                } catch (IndexOutOfBoundsException ex) {
                    temp.add(data);
                }
                y.right = newNode;
            }
            newNode.height = y.height + 1;
            leaves.add(newNode);
            leaves.remove(y);
            waysTo.put(newNode, temp);
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
            Set<Integer> toDel = new TreeSet<>();
            int min = BinaryTree.leaves.stream().map(BinaryTree.Node::getHeight).min(Integer::compareTo).get();
            if (min % 2 == 0)
                bt.iterativePreorder();
            else {
                BinaryTree.leaves.removeIf(o -> o.getHeight() > min);
                for (BinaryTree.Node i : BinaryTree.leaves) {
                    toDel.add(BinaryTree.waysTo.get(i).get(min / 2));
                }
                for (int i : toDel) {
                    bt.remove(i);
                }
                bt.iterativePreorder();
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }


    //bt.iterativePreorder();

}

