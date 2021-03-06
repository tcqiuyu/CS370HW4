

public class Consumer implements Runnable {
	
	private static final int MAX_SIZE = 1000000;

	private Buffer buffer;
	private Double extractSum = 0.0;
	private int extractNum = 0;

	public Consumer(Buffer b) {
		buffer = b;
	}

	//return the number of consumed elements
	public int getConsNum() {
		return extractNum;
	}

	//printout the verification message
	public void printCumVal() {
		System.out.printf("Consumer: Consumed %d items,  Cumulative value of consumed items=%f\n", extractNum, extractSum);
	}

	@Override
	public void run() {
		Double extractElement;
		while (this.getConsNum() <= MAX_SIZE) {

			switch (this.getConsNum()) {
			case 100000:
			case 200000:
			case 300000:
			case 400000:
			case 500000:
			case 600000:
			case 700000:
			case 800000:
			case 900000:
			case 1000000:
				extractElement = buffer.consume();
				if (extractElement != null) {
					this.printCumVal();
					extractSum += extractElement;
					extractNum++;
				}
				break;
			default:
				extractElement = buffer.consume();
				if (extractElement != null) {
					extractSum += extractElement;
					extractNum++;
				}
//				this.printCumVal();
				break;
			}
		}
		System.out.println("Consumer: Finished consuming 1,000,000 items");
	}
}
