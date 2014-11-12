package cs370HW4;

public class Buffer {

	private static final int BUFFER_SIZE = 10;

	private Double[] buffer;
	private int in, out;

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

	public void printout() {
		for (int i = 0; i < BUFFER_SIZE; i++) {
			System.out.println("Index " + i + "=" + buffer[i]);
		}
	}

	public void produce(Double newElement) {

		if ((in + 1) % BUFFER_SIZE == out) {// if buffer is full
			synchronized (producer) {
				try {
					producer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		} else {// otherwise

			synchronized (consumer) {
				buffer[in] = newElement;
				in = (in + 1) % BUFFER_SIZE;
				for (int i = 0; i < BUFFER_SIZE; i++) {
					System.out.print("Produce No. "+producer.getProdNum()+"----");
					System.out.println("Index " + i + "=" + buffer[i]);
				}
				consumer.notify();
			}
		}
	}

	public Double consume() {
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
			synchronized (producer) {
				temp = buffer[out];
				buffer[out] = null;
				out = (out + 1) % BUFFER_SIZE;
				for (int i = 0; i < BUFFER_SIZE; i++) {
					System.out.print("Consume No. "+consumer.getConsNum()+"----");
					System.out.println("Index " + i + "=" + buffer[i]);
				}
				producer.notify();
			}
		}
		return temp;
	}

	public void initiate() {
		pthread = new Thread(producer);
		cthread = new Thread(consumer);
		pthread.start();
		cthread.start();
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
