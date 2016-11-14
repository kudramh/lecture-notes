
clc, clear all, close all
% x = 0:.000001:1;
n=0:5;
hold on,grid on
plot(n, log(n),     'r')
plot(n, sqrt(n),    'g')
plot(n, n,          'b')
plot(n, n.*log(n),  'c')
% plot(x, x.^2)
% plot(x, x.^3)
% plot(x, 2.^x)
% plot(x, factorial(x))
% 
% 
