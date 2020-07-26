import numpy as np
import datetime

open('tx.txt', 'w').close()
f = open("tx.txt", "a")
#f.write("Now the file has more content!")

nPr  = 22;
prID = range(1,nPr+1);
cuID = range(4,6);

meatCu  = 0.2*np.ones(nPr+1);meatCu[[4,10,11,13]] = 0.6;meatCu[[12,14]]=0.4;
veganCu = 0.2*np.ones(nPr+1);veganCu[[15,20]] = 0.6;veganCu[[12,13,14]] = 0.05;
junkCu  = 0.2*np.ones(nPr+1);junkCu[[19,18]] = 0.5; junkCu[[22,21]]= 0.6;


for cu in cuID:
	if cu<=2:
		P = meatCu;
	elif cu<=3:
		P = veganCu;
	else:
		P = junkCu;

	for txID in range(80,120):
		dateCu = datetime.date.today()-datetime.timedelta(days = 10*txID)
		for pr in prID:
			if np.random.uniform()<P[pr]:
				f.write(str(txID+1)+','+str(cu)+','+'history,'+str(pr)+','+str(int(3*np.random.uniform())+1)+','+str(dateCu)+',user \n')
				#f.write(str(pr))



f.close()