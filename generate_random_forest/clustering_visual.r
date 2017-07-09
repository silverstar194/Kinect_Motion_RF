library(scatterplot3d)

x = c()
y = c()
z = c()
for(i in 1:166){
  x = c(x, colors_w$`0`[i])
  y = c(y, colors_w$`1`[i])
  z = c(z, colors_w$`0_1`[i])
}

data=data.frame(x,y,z)

scatterplot3d(data$x,data$y,data$z)