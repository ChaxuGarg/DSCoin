package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF obj = new CRF(64);
    if(!tB.dgst.substring(0, 4).equals("0000")) return false;
    String prev_dgst = "";
    if(tB.previous == null) prev_dgst = start_string;
    else prev_dgst = tB.previous.dgst;
    if(!tB.dgst.equals(obj.Fn(prev_dgst + "#" + tB.trsummary + "#" + tB.nonce))) return false;
    MerkleTree tree = new MerkleTree();
    String summary = tree.Build(tB.trarray);
    if(!tB.trsummary.equals(summary)) return false;
    for(int i = 0; i < tB.trarray.length; i++) {
      if(!tB.checkTransaction(tB.trarray[i])) return false;
    }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    int longest = 0;
    TransactionBlock lastValidBlock = this.lastBlocksList[0];
    for(int i = 0; i < this.lastBlocksList.length; i++) {
      if(this.lastBlocksList[i] == null) break;
      TransactionBlock curr = this.lastBlocksList[i];
      TransactionBlock validBlock = this.lastBlocksList[i];
      int length = 0;
      while(curr != null) {
        length += 1;
        if(!checkTransactionBlock(curr)) {
          length = 0;
          validBlock = curr.previous;
        }
        curr = curr.previous;
      }
      if(length > longest) {
        longest = length;
        lastValidBlock = validBlock;
      }
    }
    return lastValidBlock;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock = this.FindLongestValidChain();
    String prev_dgst = "";
    CRF obj = new CRF(64);
    if(lastBlock == null) prev_dgst = start_string;
    else prev_dgst = lastBlock.dgst;
    int nonce_temp = 1000000001;
    String dgst = obj.Fn(prev_dgst + "#" + newBlock.trsummary + "#" + Integer.toString(nonce_temp));
    while(!dgst.substring(0, 4).equals("0000")) {
      nonce_temp += 1;
      dgst = obj.Fn(prev_dgst + "#" + newBlock.trsummary + "#" + Integer.toString(nonce_temp));
    }
    newBlock.nonce = Integer.toString(nonce_temp);
    newBlock.dgst = dgst;
    newBlock.previous = lastBlock;
    Boolean added = false;
    for(int i = 0; i < this.lastBlocksList.length; i++) {
      if(this.lastBlocksList[i] == null) {
        this.lastBlocksList[i] = newBlock;
        break;
      }
      if(this.lastBlocksList[i] == lastBlock) {
        this.lastBlocksList[i] = newBlock;
        added = true;
        break;
      }
    }
    return;
  }
}
