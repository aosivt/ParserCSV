foreach product in csv -> N {
  // find count product 
  prodCount = 0
  foreach p in pq { // O(k)
    if p == product {
      prodCount++
			if prodCount > K { break }
   	}
  }

  // Add to queue
  if prodCount < K { 
    // O(log(N)) 
    pq.add(product)  
  }
  
}

createCSV
pqCount = 0
// O(M*log(M))
foreach productPQ in pq  {
if pqCount > M {break;}
  // O(log(M))   + O(1)           = O(log(M))
  pq.poll() and remove min element
  pqCount ++
}

T(N,M,K) = O(N*log(N))+O(M*log(M))
