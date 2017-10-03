data Btree = Leaf | Node (Btree) Int (Btree)
  deriving (Show)

insert :: Int -> Btree -> Btree
insert n Leaf = (Node Leaf n Leaf)
insert n (Node l x r)
  |n < x = Node (insert n l) x r
  |n > x = Node l x (insert n r)
  |n == x = (Node l x r)

treeToList :: Btree -> [Int]
treeToList Leaf = []
treeToList (Node l x r) = treeToList l ++ [x] ++ treeToList r

sumTree :: Btree -> Int
sumTree Leaf = 0
sumTree (Node l x r) = sumTree l + x + sumTree r

listToTree :: [Int] -> Btree
listToTree [] = Leaf
listToTree (x:xs) =
  Node (listToTree (filter (<x) xs)) x (listToTree(filter (>x) xs))
