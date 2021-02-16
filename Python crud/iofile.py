from .models import Records
import csv

class IOF:
    def load_file():
            Records.objects.all().delete()  
            try:                
                reader = csv.reader(open('source_csv/13100262.csv', newline=''))
                reader.__next__()  # skip column names
            except FileNotFoundError:
                print("File not found")
            except IOError:
                print("Error reading the file")
            except:
                print("An exception has occurred")

            for i in range(200):
                try:
                    myData = reader.__next__()
                    Records.objects.create(ref_date = myData.pop(0),
                                    geo = myData.pop(0),
                                    dguid = myData.pop(0),
                                    sex = myData.pop(0),
                                    age_group = myData.pop(0),
                                    student_response = myData.pop(0),
                                    uom = myData.pop(0),
                                    uom_id = myData.pop(0),
                                    scalar_factor = myData.pop(0),
                                    scalar_id = myData.pop(0),
                                    vector = myData.pop(0),
                                    coordinate = myData.pop(0),
                                    value = myData.pop(0),
                                    status = myData.pop(0),
                                    symbol = myData.pop(0),
                                    terminated = myData.pop(0),
                                    decimals = myData.pop(0))
                except:
                    print("End of records")


    def write_file():
        #adapted from https://www.programiz.com/python-programming/writing-csv-files
        with open('source_csv/new_records.csv', 'w', newline='') as file:
            
            writer = csv.writer(file)
            for record in Records.objects.all():
                writer.writerow([str(record)])


import threading, time, random, queue, csv

class Prod:
	"""
	Producer class
	Read records from CSV and add into a queue
	"""
	def __init__(self):
		self.reader = csv.reader(open('13100262.csv', newline=''))
		self.reader.__next__()  # skip column names
		

	def run(self):
		"""
		run method that is called from the thread
		"""
		global q
		try:
			while(True):
				if q.not_full:
					record = self.reader.__next__()
					q.put(record)
					print("Reading: {}".format(record))
				else:
					print("Producer is waiting")
		except StopIteration:
			print("End of file")

class Comnsumer:
	"""
	Consumer Class
	Read records from queue and write into a CSV file
	"""
	def __init__(self):
		self.writer = csv.writer(open('new_records.csv', 'w', newline=''))


	def run(self):
		"""
		run method that is called from the thread
		"""
		global q
		global pt
		while(pt.is_alive() or q.not_empty):
			print('Queue size: {}'.format(q.qsize()))
			if q.not_empty:
				record = q.get()
				self.writer.writerow(record)
				print("Writing: {}".format(record))
			else:
				print('Consumer is waiting for producer!')

if __name__ == '__main__':
	q = queue.Queue(5)  # define a queue of size 5
	p = Prod()  # instantiate the producer
	c = Comnsumer()  # instantiate the consumer
	pt = threading.Thread(target=p.run, args = ())  # instance of producer thread
	ct = threading.Thread(target=c.run, args = ())  # instance of consumer thread
	pt.start()  # starts producer thread
	ct.start()  # starts consumer thread