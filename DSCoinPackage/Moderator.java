package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int coin = 100000;
    int no_members = DSObj.memberlist.length;
    int per_person = coinCount/no_members;
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    int x = 0;
    while(x < coinCount) {
      for(int j = 0; j < no_members && x < coinCount; j++) {
        Transaction t = new Transaction();
        t.coinID = Integer.toString(coin);
        t.Source = Moderator;
        t.Destination = DSObj.memberlist[j];
        t.coinsrc_block = null;
        DSObj.pendingTransactions.AddTransactions(t);
        coin++;
        x++;
      }
    }
    while(DSObj.pendingTransactions.numTransactions != 0) {
      Transaction[] temp = new Transaction[DSObj.bChain.tr_count];
      for(int i = 0; i < DSObj.bChain.tr_count; i++) {
        try {
          temp[i] = DSObj.pendingTransactions.RemoveTransaction();
        } catch(Exception e) {
          System.out.println(e);
        }
      }
      TransactionBlock curr = new TransactionBlock(temp);
      for(int i = 0; i < DSObj.bChain.tr_count; i++) {
        temp[i].Destination.mycoins.add(new Pair<String, TransactionBlock>(temp[i].coinID, curr));
      }
      DSObj.bChain.InsertBlock_Honest(curr);
    }
    DSObj.latestCoinID = Integer.toString(coin - 1);
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int coin = 100000;
    int no_members = DSObj.memberlist.length;
    int per_person = coinCount/no_members;
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    int x = 0;
    while(x < coinCount) {
      for(int j = 0; j < no_members && x < coinCount; j++) {
        Transaction t = new Transaction();
        t.coinID = Integer.toString(coin);
        t.Source = Moderator;
        t.Destination = DSObj.memberlist[j];
        t.coinsrc_block = null;
        DSObj.pendingTransactions.AddTransactions(t);
        coin++;
        x++;
      }
    }
    while(DSObj.pendingTransactions.numTransactions != 0) {
      Transaction[] temp = new Transaction[DSObj.bChain.tr_count];
      for(int i = 0; i < DSObj.bChain.tr_count; i++) {
        try {
          temp[i] = DSObj.pendingTransactions.RemoveTransaction();
        } catch(Exception e) {
          System.out.println(e);
        }
      }
      TransactionBlock curr = new TransactionBlock(temp);
      for(int i = 0; i < DSObj.bChain.tr_count; i++) {
        temp[i].Destination.mycoins.add(new Pair<String, TransactionBlock>(temp[i].coinID, curr));
      }
      DSObj.bChain.InsertBlock_Malicious(curr);
    }
    DSObj.latestCoinID = Integer.toString(coin - 1);
  }
}
