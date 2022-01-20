package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(this.firstTransaction == null) {
      this.firstTransaction = transaction;
      this.lastTransaction = transaction;
      this.numTransactions = 1;
    } else if (this.numTransactions == 1) {
      this.firstTransaction = transaction;
      this.firstTransaction.next = this.lastTransaction;
      this.lastTransaction.previous = this.firstTransaction;
      this.numTransactions += 1;
    } else {
      this.firstTransaction.previous = transaction;
      transaction.next = this.firstTransaction;
      this.firstTransaction = transaction;
      this.numTransactions += 1;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction last;
    if(this.firstTransaction == null) throw new EmptyQueueException();
    else if (this.numTransactions == 1) {
      this.numTransactions = 0;
      last = this.lastTransaction;
      this.firstTransaction = null;
      this.lastTransaction = null;
    } else if(this.numTransactions == 2) {
      last = this.lastTransaction;
      this.firstTransaction.next = null;
      this.lastTransaction = this.firstTransaction;
      this.numTransactions -= 1;
    } else {
      last = this.lastTransaction;
      this.lastTransaction.previous.next = null;
      this.lastTransaction = this.lastTransaction.previous;
      this.numTransactions -= 1;
    }
    return last;
  }

  public int size() {
    if(this.firstTransaction == null) return 0;
    return this.numTransactions;
  }
}
