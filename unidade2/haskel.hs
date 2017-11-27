module Main where

import Control.Concurrent
import Control.Concurrent.MVar

makeBulbo :: MVar int -> IO()
makeBulbo bulbo =
  do
    p <- takeMVar bulbo
    putMVar bulbo(p+1)
    putStrln("produzindo bulbo")

makeSocket :: MVar int -> IO()
makeSocket socket =
  do
    p <- takeMVar socket
    putMVar socket(p+1)
    putStrln("produzindo Socket")

makeEmbalagem :: MVar int -> IO()
makeEmbalagem embalagem =
  do
    p <- takeMVar embalagem
    putMVar embalagem(p+1)
    putStrln("produzindo embalagem")

makeLampada :: MVar int -> MVar int -> MVar int -> MVar int -> IO()
makeLampada bulbo socket lampada embalagem =
  do
    b <- takeMVar bulbo
    s <- takeMVar socket
    l <- takeMVar lampada
    e <- takeMVar embalagem

    putMVar bulbo(b-1)
    putMVar socket(s-1)
    putMVar lampada(l+1)
    putMVar embalagem(e-1)
    putStrln("produzindo lampada")

encaixota :: MVar int -> MVar int -> MVar int -> IO()
encaixota lampada caixa deposito =
  do
    l <- takeMVar lampada
    c <- takeMVar caixa

    if c >= 50
    then
      do
        c <- 0
        d <- takeMVar deposito
        putMVar deposito(d+1)
        putMVar lampada(l-1)
        putMVar caixa(c+1)
        putStrln("Encaixotando lampada")
    else
      do
        putMVar lampada(l-1)
        putMVar caixa(c+1)
        putStrln("Encaixotando lampada")

main :: IO()
main =
  do
    bulbo <- new MVar 0
    socket <- new MVar 0
    lampada <- new MVar 0
    embalagem <- new MVar 0
    caixa <- new MVar 0
    deposito <- new MVar 0

    forkIO(makeBulbo bulbo)
    forkIO(makeSocket socket)
    forkIO(makeEmbalagem embalagem)
    forkIO(makeLampada bulbo socket lampada embalagem)
    forkIO(encaixota lampada caixa deposito)

    main
