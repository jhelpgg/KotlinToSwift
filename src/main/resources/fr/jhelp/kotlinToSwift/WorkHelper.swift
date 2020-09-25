import Dispatch

typealias Long = Int64

extension String
{
    func isNotEmpty() -> Bool
    {
       return !self.isEmpty
    }
}

extension Int
{
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

extension Long
{
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

extension Double
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

extension Array
{
    func firstOrNull(_ filter:(Element)->Bool) -> Element?
    {
        return self.first(where: filter)
    }
}

class Math
{
    static func random() -> Double
    {
        return Double.random(in: 0 ..< 1)
    }
}

enum CommonManagedExceptions : Error
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