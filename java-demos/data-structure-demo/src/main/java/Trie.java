/**
 * @author : wh
 * @date : 2024/1/19 19:45
 * @description: 前缀树
 */
public class Trie {

    private final TrieNode trieNode = new TrieNode('/');

    /**
     * 插入字符串
     */
    public void insert(char[] text) {
        TrieNode p = trieNode;
        for (char c : text) {
            int index = c - 'a';
            if (p.children[index] == null) {
                TrieNode newNode = new TrieNode(c);
                p.children[index] = newNode;
            }

            p = p.children[index];

        }
        p.isEndingChar = true;

    }

    public boolean find(char[] pattern) {
        TrieNode p = trieNode;
        for (char c : pattern) {
            int index = c - 'a';
            if (p.children[index] == null) {
                return false;
            }
            p = p.children[index];
        }
        return p.isEndingChar;
    }

}

class TrieNode {
    char data;

    TrieNode[] children;

    public boolean isEndingChar = false;

    public TrieNode(char data) {
        this.data = data;
        this.children = new TrieNode[26];
    }
}
