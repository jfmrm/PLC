module Main where

import Control.Concurrent
import Control.Concurrent.STM

type Account = TVar Int

saque :: Account -> Int -> STM()
saque account value =
  do
    s <- readTVar account
    writeTVar account(s-value)

deposit :: Account -> Int -> STM()
deposit account value =
  do
    saque account (value * (-1))

saque2 :: Account -> Int -> STM ()
saque2 account value =
  do
    s <- readTVar account
    if s < value then
      retry
    else
      writeTVar account(s - value)

saque3 :: Account -> Account -> Int -> STM()
saque3 accountA accountB value =
  do
    orElse (saque2 accountA value) (saque2 accountB value)

main :: IO()
main =
  do
    accA <- atomically(newTVar 200)
    accB <- atomically(newTVar 300)

    --Call funtions here
    vA <- atomically(readTVar accA)
    vB <- atomically(readTVar accB)
    putStrLn(show vA)
    putStrLn(show vB)
