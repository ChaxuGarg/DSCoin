package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Pair<String, TransactionBlock> coin = this.mycoins.get(0);
    Members dest = new Members();
    for(int i = 0; i < DSobj.memberlist.length; i++){
      if(DSobj.memberlist[i].UID.equals(destUID)) {
        dest = DSobj.memberlist[i];
        break;
      }
    }
    this.mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = coin.first;
    tobj.Source = this;
    tobj.Destination = dest;
    tobj.coinsrc_block = coin.second;
    if(this.in_process_trans == null) this.in_process_trans = new Transaction[100];
    for(int i = 0; i < in_process_trans.length; i++) {
      if(this.in_process_trans[i] == null) {
        this.in_process_trans[i] = tobj;
        break;
      }
    }
    DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String, TransactionBlock> coin = this.mycoins.get(0);
    Members dest = new Members();
    for(int i = 0; i < DSobj.memberlist.length; i++){
      if(DSobj.memberlist[i].UID.equals(destUID)) {
        dest = DSobj.memberlist[i];
        break;
      }
    }
    this.mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = coin.first;
    tobj.Source = this;
    tobj.Destination = dest;
    tobj.coinsrc_block = coin.second;
    if(this.in_process_trans == null) this.in_process_trans = new Transaction[100];
    for(int i = 0; i < in_process_trans.length; i++) {
      if(this.in_process_trans[i] == null) {
        this.in_process_trans[i] = tobj;
        break;
      }
    }
    DSobj.pendingTransactions.AddTransactions(tobj);
  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock tB = DSObj.bChain.lastBlock;
    int t_idx = 0;
    while(tB != null) {
      Boolean k = false;
      for(int i = 0; i < tB.trarray.length; i++) {
        if(tB.trarray[i] == tobj) {
          t_idx = i;
          k = true;
          break;
        }
      }
      if(k) break;
      tB = tB.previous;
    }
    if(tB == null) throw new MissingTransactionException();
    int n = tB.trarray.length;
		TreeNode curr = tB.Tree.rootnode;
		List<Pair<String, String>> path1 = new ArrayList<Pair<String, String>>();
    t_idx++;
		while(n > 2) {
			if(t_idx > n/2){
				curr = curr.right;
				t_idx -= n/2;
			} else curr = curr.left;
			n = n/2;
		}

		while(curr != null) {
			Pair<String, String> temp = new Pair<String, String>(curr.left.val, curr.right.val);
			path1.add(temp);
			curr = curr.parent;
		}
    path1.add(new Pair<String, String>(tB.Tree.rootnode.val, null));
    TransactionBlock curr2 = DSObj.bChain.lastBlock;
    List<Pair<String, String>> path2 = new ArrayList<Pair<String, String>>();
    while(curr2 != tB.previous) {
      Pair<String, String> temp = new Pair<String, String>(curr2.dgst, curr2.previous.dgst + "#" + curr2.trsummary + "#" + curr2.nonce);
      path2.add(temp);
      curr2 = curr2.previous;
    }
    if(curr2 != null) path2.add(new Pair<String, String>(curr2.dgst, null));
    else path2.add(new Pair<String, String>("Genesis", null));
    Collections.reverse(path2);
    for(int i = 0; i < this.in_process_trans.length; i++) {
      if(this.in_process_trans[i] == tobj) {
        this.in_process_trans[i] = null;
        break;
      }
    }
    int i = 0;
    if(tobj.Destination.mycoins.size() == 0) {
      tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, tB));
      return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
    }
    else if(tobj.Destination.mycoins.get(tobj.Destination.mycoins.size() - 1).first.compareTo(tobj.coinID) < 0) {
      tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, tB));
      return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
    }
    while(tobj.Destination.mycoins.get(i).first.compareTo(tobj.coinID) < 0) {
      i++;
    }
    tobj.Destination.mycoins.add(i, new Pair<String, TransactionBlock>(tobj.coinID, tB));
    return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Malicious DSObj) throws MissingTransactionException {
    TransactionBlock tB = DSObj.bChain.FindLongestValidChain();
    int t_idx = 0;
    while(tB != null) {
      Boolean k = false;
      for(int i = 0; i < tB.trarray.length; i++) {
        if(tB.trarray[i] == tobj) {
          t_idx = i;
          k = true;
          break;
        }
      }
      if(k) break;
      tB = tB.previous;
    }
    if(tB == null) throw new MissingTransactionException();
    int n = tB.trarray.length;
		TreeNode curr = tB.Tree.rootnode;
		List<Pair<String, String>> path1 = new ArrayList<Pair<String, String>>();
    t_idx++;
		while(n > 2) {
			if(t_idx > n/2){
				curr = curr.right;
				t_idx -= n/2;
			} else curr = curr.left;
			n = n/2;
		}

		while(curr != null) {
			Pair<String, String> temp = new Pair<String, String>(curr.left.val, curr.right.val);
			path1.add(temp);
			curr = curr.parent;
		}
    path1.add(new Pair<String, String>(tB.Tree.rootnode.val, null));
    TransactionBlock curr2 = DSObj.bChain.FindLongestValidChain();
    List<Pair<String, String>> path2 = new ArrayList<Pair<String, String>>();
    while(curr2 != tB.previous) {
      Pair<String, String> temp = new Pair<String, String>(curr2.dgst, curr2.previous.dgst + "#" + curr2.trsummary + "#" + curr2.nonce);
      path2.add(temp);
      curr2 = curr2.previous;
    }
    if(curr2 != null) path2.add(new Pair<String, String>(curr2.dgst, null));
    else path2.add(new Pair<String, String>("Genesis", null));
    Collections.reverse(path2);
    for(int i = 0; i < this.in_process_trans.length; i++) {
      if(this.in_process_trans[i] == tobj) {
        this.in_process_trans[i] = null;
        break;
      }
    }
    int i = 0;
    if(tobj.Destination.mycoins.size() == 0) {
      tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, tB));
      return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
    }
    else if(tobj.Destination.mycoins.get(tobj.Destination.mycoins.size() - 1).first.compareTo(tobj.coinID) < 0) {
      tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, tB));
      return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
    }
    while(tobj.Destination.mycoins.get(i).first.compareTo(tobj.coinID) < 0) {
      i++;
    }
    tobj.Destination.mycoins.add(i, new Pair<String, TransactionBlock>(tobj.coinID, tB));
    return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path1, path2);
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    Transaction[] transactions = new Transaction[DSObj.bChain.tr_count];
    int i = 0;
    while(i != DSObj.bChain.tr_count-1) {
      if(!DSObj.bChain.lastBlock.checkTransaction(DSObj.pendingTransactions.lastTransaction)) {
        try {
          DSObj.pendingTransactions.RemoveTransaction();
        } catch(Exception e) {
          System.out.println(e);
        }
        continue;
      }
      Boolean k = false;
      for(int j = 0; j < transactions.length; j++) {
        if(transactions[j] == null) break;
        if(DSObj.pendingTransactions.lastTransaction.coinID.equals(transactions[j].coinID)) {
          try {
            DSObj.pendingTransactions.RemoveTransaction();
          } catch(Exception e) {
            System.out.println(e);
          }
          k = true;
          break;
        }
      }
      if(k) continue;
      try {
        transactions[i] = DSObj.pendingTransactions.RemoveTransaction();
      } catch (Exception e) {
        System.out.println(e);
      }
      i++;
    }   
    DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
    transactions[i] = new Transaction();
    transactions[i].coinID = DSObj.latestCoinID;
    transactions[i].Source = null;
    transactions[i].Destination = this;
    transactions[i].coinsrc_block = null;
    TransactionBlock newBlock = new TransactionBlock(transactions);
    DSObj.bChain.InsertBlock_Honest(newBlock);
    if(this.mycoins.size() == 0) {
      this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
      return;
    }
    if(this.mycoins.get(this.mycoins.size()-1).first.compareTo(DSObj.latestCoinID) < 0) {
      this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
      return;
    }
    i = 0;
    while(this.mycoins.get(i).first.compareTo(DSObj.latestCoinID) < 0) {
      i++;
    }
    this.mycoins.add(i, new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
    return;
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
    Transaction[] transactions = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock lastBlock = DSObj.bChain.FindLongestValidChain();
    int i = 0;
    while(i != DSObj.bChain.tr_count-1) {
      if(!lastBlock.checkTransaction(DSObj.pendingTransactions.lastTransaction)) {
        try {
          DSObj.pendingTransactions.RemoveTransaction();
        } catch(Exception e) {
          System.out.println(e);
        }
        continue;
      }
      Boolean k = false;
      for(int j = 0; j < transactions.length; j++) {
        if(transactions[j] == null) break;
        if(DSObj.pendingTransactions.lastTransaction.coinID.equals(transactions[j].coinID)) {
          try {
            DSObj.pendingTransactions.RemoveTransaction();
          } catch(Exception e) {
            System.out.println(e);
          }
          k = true;
          break;
        }
      }
      if(k) continue;
      try {
        transactions[i] = DSObj.pendingTransactions.RemoveTransaction();
      } catch (Exception e) {
        System.out.println(e);
      }
      i++;
    }
    DSObj.latestCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
    transactions[i] = new Transaction();
    transactions[i].coinID = DSObj.latestCoinID;
    transactions[i].Source = null;
    transactions[i].Destination = this;
    transactions[i].coinsrc_block = null;
    TransactionBlock newBlock = new TransactionBlock(transactions);
    DSObj.bChain.InsertBlock_Malicious(newBlock);
    if(this.mycoins.size() == 0) {
      this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
      return;
    }
    if(this.mycoins.get(this.mycoins.size()-1).first.compareTo(DSObj.latestCoinID) < 0) {
      this.mycoins.add(new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
      return;
    }
    i = 0;
    while(this.mycoins.get(i).first.compareTo(DSObj.latestCoinID) < 0) {
      i++;
    }
    this.mycoins.add(i, new Pair<String, TransactionBlock>(DSObj.latestCoinID, newBlock));
  }  
}
