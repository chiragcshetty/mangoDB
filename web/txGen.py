import numpy as np
import datetime

open('tx.txt', 'w').close()
f = open("tx.txt", "a")
#f.write("Now the file has more content!")

nPr  = 22;
prID = range(1,nPr+1);
cuID = range(2,4);

meatCu  = 0.2*np.ones(nPr+1);meatCu[[4,10,11,13]] = 0.6;meatCu[[12,14]]=0.4;
veganCu = 0.2*np.ones(nPr+1);veganCu[[15,20]] = 0.6;veganCu[[12,13,14]] = 0.05;
junkCu  = 0.2*np.ones(nPr+1);junkCu[[19,18]] = 0.5; junkCu[[22,21]]= 0.6;


for cu in cuID:
	P = np.zeros(nPr+1); P[[1,2,3,4,5]]=0.7; P[[6,7,8,9,10,11,12,13]]=0.4;P[[14,15,16,17,18,19,20,21]]=0.2;
	prCnt = np.ones(nPr+1); prAvg = np.zeros(nPr+1); prAvg[[2,4,6,8,10,12]] = 0.5; prAvg[[1,3,5,7,9,11]] = 1; prAvg[range(13,nPr+1)] = 2;

	print(prAvg)

	dateCu = datetime.date.today()
	for txID in range(1000,1100):
		txID = 1100-txID;
		dateCu = dateCu-datetime.timedelta(days = 7 + round(1*np.random.randn()))
		for pr in prID:
			if np.random.uniform()<P[pr]:
				f.write(str(txID+1)+','+str(cu)+','+'history,'+str(pr)+','+str(int(prCnt[pr] + np.random.uniform()))+','+str(dateCu)+',user \n')
				prCnt[pr] = 1;
			else:
				prCnt[pr] += prAvg[pr];



f.close()