package cs370HW4;

import java.util.Random;

public class Producer implements Runnable {

	private Double prodSum = 0.0;
	private int prodNum = 0;
	private Buffer buffer;

	public Producer(Buffer b) {
		buffer = b;
	}

	private Double generateElement() {
		Random random = new Random();
		Double bufferElement = random.nextDouble() * 100.0;
		prodSum += bufferElement;
		prodNum++;
		return bufferElement;
	}

	public void printCumVal() {
		System.out.println("Producer: Produced " + prodNum + " items, Cummulative value of produced items=" + prodSum
				+ "\n");
	}

	public int getProdNum() {
		return prodNum;
	}

	@Override
	public void run() {

		Double newElement;
		while (this.getProdNum() < 10) {
			// try {
			// buffer.produceConsume();
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			//
			// switch (this.getProdNum()) {
			// case 100000:
			// case 200000:
			// case 300000:
			// case 400000:
			// case 500000:
			// case 600000:
			// case 700000:
			// case 800000:
			// case 900000:
			// case 1000000s:
			// this.printCumVal();
			// newElement = this.generateElement();
			// buffer.add(buffer.getIn(), newElement);
			// break;
			// default:
				
				newElement = this.generateElement();
				buffer.produce(buffer.getIn(), newElement);
				this.printCumVal();
			// System.out.println("Current ran = " + newElement);
			// break;
			// }
		}
		// System.out.println(this.getProdNum());
		// buffer.printout();
	}
}
