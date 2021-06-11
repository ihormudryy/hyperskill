import java.util.Arrays;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int lines = Integer.parseInt(scanner.nextLine());
    Tree<Integer> treeRoot = new Tree(0);
    for (int i = 0; i < lines; i++) {
      int[] nodes = Arrays.stream(scanner.nextLine().split("\\s+"))
                          .mapToInt(Integer::parseInt).toArray();
      Tree<Integer> pointer = treeRoot.findNode(treeRoot, nodes[0]);
      if (pointer != null && nodes.length > 1) {
        if (pointer.getLeft() == null) {
          pointer.putLeft(new Tree<>(nodes[1]));
        } else if (pointer.getRight() == null) {
          pointer.putRight(new Tree<>(nodes[1]));
        } else {
          System.out.println("no");
          return;
        }
      }
    }
    System.out.println(treeRoot.isBinaryTree(treeRoot) ? "yes" : "no");
  }

  public static class Tree<T> {

    Integer number;
    Tree<T> left, right;

    public Tree(Integer number) {
      this.number = number;
    }

    public static Tree findNode(Tree node, Integer number) {
      if (node == null) {
        return null;
      }

      if (node.getNumber().equals(number)) {
        return node;
      }

      Tree left = node.findNode(node.getLeft(), number);
      if (node.findNode(node.getLeft(), number) != null) {
        return left;
      }

      Tree right = node.findNode(node.getRight(), number);
      if (node.findNode(node.getRight(), number) != null) {
        return right;
      }

      return null;
    }

    public static boolean isBinaryTree(Tree node) {
      if (node == null) {
        return true;
      }

      if ((node.getRight() == null && node.getLeft() != null)
          || (node.getRight() != null && node.getLeft() == null)) {
        return false;
      }

      boolean leftBinaryTree = isBinaryTree(node.getLeft());
      if (leftBinaryTree == false) {
        return false;
      }

      boolean rightBinaryTree = isBinaryTree(node.getRight());
      if (rightBinaryTree == false) {
        return false;
      }

      return true;
    }

    public void putLeft(Tree<T> left) {
      this.left = left;
    }

    public void putRight(Tree<T> right) {
      this.right = right;
    }

    public Tree<T> getLeft() {
      return left;
    }

    public Tree<T> getRight() {
      return right;
    }

    public Integer getNumber() {
      return number;
    }
  }
}