import Foundation

public typealias Long = Int64

public typealias Byte = Int8

public extension String
{
    var length : Int { get { return self.count } }

    func isEmpty() -> Bool
    {
       return self.isEmpty
    }

    func isNotEmpty() -> Bool
    {
       return !self.isEmpty
    }

    func toInt() -> Int
    {
        return Int(self) ?? 0
    }

    func startsWith(_ start:String) -> Bool
    {
        return self.hasPrefix(start)
    }

    func indexOf(_ searched:String) -> Int
    {
        return self.indexOf(searched, 0)
    }

    func indexOf(_ searched:String, _ index:Int) -> Int
    {
        if(searched.isEmpty)
        {
            return index
        }

        let selfArray = Array(self.unicodeScalars)
        let searchedArray = Array(searched.unicodeScalars)
        let limit = selfArray.count - searchedArray.count + 1
        var index = index
        var check = 0
        var look = 0

        while(index<limit)
        {
            if(selfArray[index] == searchedArray[0])
            {
                check = index + 1
                look = 1

                while(look < searched.count)
                {
                    if(selfArray[check] != searchedArray[look])
                    {
                        break
                    }

                    look = look + 1
                    check = check + 1
                }

                if(look >= searched.count)
                {
                    return index
                }
            }

            index = index + 1
        }

        return -1
    }

    func substring(_ start : Int) -> String
    {
        return String(self[String.Index(encodedOffset:start) ..< String.Index(encodedOffset:self.count)])
    }

    func substring(_ start : Int, _ end : Int) -> String
    {
        return String(self[String.Index(encodedOffset:start) ..< String.Index(encodedOffset:end)])
    }
}

public extension Int
{
   func toByte() -> Byte
   {
      return Byte(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Byte
{
   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Long
{
   func toByte() -> Byte
   {
      return Byte(self)
   }

   func toInt() -> Int
   {
       return Int(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }

   func toDouble() -> Double
   {
      return Double(self)
   }
}

public extension Float
{
   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toDouble() -> Double
   {
       return Double(self)
   }
}


public extension Double
{
   static var POSITIVE_INFINITY : Double { get {  return 1.0 / 0.0} }
   static var NEGATIVE_INFINITY : Double { get {  return -1.0 / 0.0} }

   func toInt() -> Int
   {
       return Int(self)
   }

   func toLong() -> Long
   {
       return Long(self)
   }

   func toFloat() -> Float
   {
       return Float(self)
   }
}

public extension Array
{
    func firstOrNull(_ filter:(Element)->Bool) -> Element?
    {
        return self.first(where: filter)
    }
}

public class Math
{
    static func random() -> Double
    {
        return Double.random(in: 0 ..< 1)
    }
}

public enum CommonManagedExceptions : Error
{
    case Exception(_ message:String)
    case IllegalArgumentException(_ message:String)
    case IllegalStateException(_ message:String)
    case RuntimeException(_ message:String)
    case NullPointerException(_ message:String)
}

public class Mutex
{
    private let mutex : DispatchSemaphore

    public init()
    {
        self.mutex = DispatchSemaphore(value : 1)
    }

    public func safeExecute(task:()->Void)
    {
        self.mutex.wait()
        task()
        self.mutex.signal()
    }
}

public func timeSince1970InMilliseconds() -> Long
{
    return Long(NSDate().timeIntervalSince1970 * 1000.0)
}

public func fatal<T>(_ message:String) -> T
{
   fatalError(message)
}
