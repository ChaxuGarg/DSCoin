package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
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
    if(this.lastBlock != null) newBlock.previous = this.lastBlock;
    this.lastBlock = newBlock;
  }
}
