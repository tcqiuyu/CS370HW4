package cs370HW4;

public class Buffer {

	private static final int BUFFER_SIZE = 10;

	private Double[] buffer;
	private int in, out, num;

	private Producer producer;
	private Consumer consumer;
	private Thread pthread, cthread;

	public Buffer() {
		buffer = new Double[BUFFER_SIZE];
	}

	public Double[] getBuffer() {
		return buffer;
	}

	public void link(Producer p, Consumer c) {
		producer = p;
		consumer = c;
	}

	// public void produceConsume() throws InterruptedException {
	//
	// synchronized (pthread) {
	// while ((in + 1) % BUFFER_SIZE == out) // if buffer is full
	// {
	// System.out.println("pthread wait");
	// pthread.wait();
	// }
	// System.out.println("pthread out");
	// // pthread.notify();
	// }
	//
	// synchronized (cthread) {
	// while (in == out) // if buffer is empty
	// {
	// System.out.println("cthread wait");
	// cthread.wait();
	// }
	//
	// cthread.notify();
	// }
	//
	// }

	public void printout() {
		for (int i = 0; i < BUFFER_SIZE; i++) {
			System.out.println(buffer[i]);
		}
	}

	public void produce(int index, Double newElement) {

		if ((in + 1) % BUFFER_SIZE == out) {// if buffer is full
			synchronized (producer) {
				try {
					producer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {// otherwise
			buffer[index] = newElement;
			in = (in + 1) % BUFFER_SIZE;
		}
	}

	public Double consume(int index) {
		Double temp = null;
		if (in == out) {// if buffer is empty
			synchronized (consumer) {
				try {
					consumer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			temp = buffer[index];
			buffer[index] = null;
			out = (out + 1) % BUFFER_SIZE;
		}
		return temp;
	}

	public void initiate() {
		pthread = new Thread(producer);
		cthread = new Thread(consumer);
		pthread.start();
		cthread.start();
	}

	public int getIn() {
		return in;
	}

	public int getOut() {
		return out;
	}

	public void add(int index, Double newElement) {
		buffer[index] = newElement;
		in = (in + 1) % BUFFER_SIZE;
	}

	public Double remove(int index) {
		Double temp = buffer[index];
		buffer[index] = null;
		out = (out + 1) % BUFFER_SIZE;
		return temp;
	}

	public static void main(String[] args) {
		Buffer newBuffer = new Buffer();
		Producer newProducer = new Producer(newBuffer);
		Consumer newConsumer = new Consumer(newBuffer);
		newBuffer.link(newProducer, newConsumer);
		newBuffer.initiate();
	}
}
