package cs370HW4;

public class Buffer {

	private static final int BUFFER_SIZE = 1000;

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

	public void produce() {

		if ((in + 1) % BUFFER_SIZE == out) {// if buffer is full
			synchronized (this) {//not synchronized(producer)
				try {
					wait();//not producer.wait()
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		} else {// otherwise

			synchronized (this) {
				double newElement = producer.generateElement();
				buffer[in] = newElement;
				in = (in + 1) % BUFFER_SIZE;
				notifyAll();
			}
		}
	}

	public Double consume() {
		Double temp = null;
		if (in == out) {// if buffer is empty
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			synchronized (this) {
				temp = buffer[out];
				buffer[out] = null;
				out = (out + 1) % BUFFER_SIZE;
				notifyAll();
			}
		}
		return temp;
	}

	public void initiate() {
		pthread = new Thread(producer);
		cthread = new Thread(consumer);
		pthread.start();
		cthread.start();
		try {
			pthread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
