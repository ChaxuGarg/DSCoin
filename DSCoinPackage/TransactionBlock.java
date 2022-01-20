package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    int num_trans = t.length;
    this.trarray = new Transaction[num_trans];
    for(int i = 0; i < num_trans; i++) this.trarray[i] = t[i];
    this.previous = null;
    this.Tree = new MerkleTree();
    this.trsummary = this.Tree.Build(t);
    this.nonce = null;
    this.dgst = null;
  }

  public boolean checkTransaction (Transaction t) {
    if(t.coinsrc_block == null) return true;
    TransactionBlock src = t.coinsrc_block;
    Boolean result = false; 
    for(int i = 0; i < src.trarray.length; i++) {
      if(src.trarray[i].coinID.equals(t.coinID)) {
        if(src.trarray[i].Destination.UID.equals(t.Source.UID)) {
          result = true;
          break;
        }
      }
    }
    if(!result) return result;
    TransactionBlock curr = this;
    while(curr != src && curr != null) {
      for(int i = 0; i < curr.trarray.length; i++) {
        if(curr.trarray[i].coinID.equals(t.coinID)) {
          result = false;
          break;
        }
      }
      if(!result) break;
      curr = curr.previous;
    }
    return result;
  }
}
