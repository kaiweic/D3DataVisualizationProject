import csv
import string

def parse():
    counts = {}

    with open('third-eye.tsv','r') as tsvin, open('counts.tsv', 'w') as tsvout:
        tsvin = csv.reader(tsvin, delimiter='\t')
        tsvout = csv.writer(tsvout, delimiter='\t')
        
        del_str = string.punctuation.replace('-', '')
        strip_punct = ''.maketrans('', '', del_str)
        
        for row in tsvin:
                words = row[4].replace('\\n', ' ').translate(strip_punct).upper().split()
                #print(words)
                for word in words:
                    id = (row[1], word)
                    if id not in counts:
                        counts[id] = 1
                    else:
                        counts[id] += 1
                        
        lst = sorted(counts.items(), key=lambda elm: elm[1], reverse=True)
        #print(counts)
        for elm in lst:
            tsvout.writerow((elm[0][0], elm[0][1], elm[1]))
    
if __name__ == "__main__":
    parse()