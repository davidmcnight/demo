
use phpseclib\Net\SSH2;
use phpseclib\Crypt\RSA;
define('NET_SSH2_LOGGING', 2);
try{
//            echo "SSH2 TEST MOAAPI2";
//            exit;

//            echo getenv("username");
$ssh = new SSH2('ssh.us.platform.sh');
$key = new RSA();
$key->setPassword('davey');
$key->loadKey(file_get_contents('C:\\id_rsa'));
//printNicely($key);
//            $ssh->login('ent-5o2tfjvpvapga-production-vohbr3y', $key);

//

if (!$ssh->login('ent-5o2tfjvpvapga-production-vohbr3y', $key)) {
echo 'Login Failed';
//                printNicely($key);
}else{
//echo "connected";
//
//                $db = mysqli_connect('127.0.0.1', '5o2tfjvpvapga', 'XtipHkKUvOcglvY', '5o2tfjvpvapga', '3306');
echo $ssh->exec('mysql -h127.0.0.1 -u5o2tfjvpvapga -D 5o2tfjvpvapga -pXtipHkKUvOcglvY');
sleep(5);
echo $ssh->exec('XtipHkKUvOcglvY');

//                $pdo = new \PDO("mysql:host=127.0.0.1;port=3306;dbname=5o2tfjvpvapga","5o2tfjvpvapga", "XtipHkKUvOcglvY+vvxkrx");
//                $query = "SELECT * FROM sales_order";
//                $s/tatement = $pdo->query($query);
//                $result = $statement->fetchAll(\PDO::FETCH_ASSOC);
//                printNicely($result);
}






unset($ssh);
//printNicely($ssh->getLog());






}catch (Exception $e){
printNicely($e->getMessage());
//            printNicely($e->getLine());
printNicely($e->getTraceAsString());
//            echo $ssh->getLog();
}