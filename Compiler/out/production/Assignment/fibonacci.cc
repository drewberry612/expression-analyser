n=12
previous=0
current=1
index=1
while (index <= n){
print(current)
temp=current
current=current+previous
previous=temp
index+=1
}
