package org.snobot.lib;

import edu.wpi.first.wpilibj.Preferences;

public final class PropertyManager // NOPMD
{
    private PropertyManager()
    {

    }

    public abstract static class IProperty<Type>
    {
        protected String mKey;
        protected Type mDefault;

        /**
         * Base class for a property.
         * 
         * @param aKey
         *            The key of the preference
         * @param aDefault
         *            The default value to use if it is not in the network table
         */
        public IProperty(String aKey, Type aDefault)
        {
            mKey = aKey;
            mDefault = aDefault;

            // Force a get-or-save operation. This will guarantee that
            // all the properties are added in the order they get constructed,
            // and that they will all immediately be written into the file
            // rather than have a lazy-instantiation thing going on
            getValue();
        }

        public abstract Type getValue();

        public String getKey()
        {
            return mKey;
        }
    }

    public static class DoubleProperty extends IProperty<Double>
    {

        public DoubleProperty(String aKey, double aDefault)
        {
            super(aKey, aDefault);
        }

        @Override
        public Double getValue()
        {
            if (Preferences.getInstance().containsKey(mKey))
            {
                return Preferences.getInstance().getDouble(mKey, mDefault);
            }

            Preferences.getInstance().putDouble(mKey, mDefault);
            return mDefault;
        }

    }

    public static class IntegerProperty extends IProperty<Integer>
    {

        public IntegerProperty(String aKey, int aDefault)
        {
            super(aKey, aDefault);
        }

        @Override
        public Integer getValue()
        {
            if (Preferences.getInstance().containsKey(mKey))
            {
                return Preferences.getInstance().getInt(mKey, mDefault);
            }

            Preferences.getInstance().putInt(mKey, mDefault);
            return mDefault;
        }
    }

    public static class StringProperty extends IProperty<String>
    {

        public StringProperty(String aKey)
        {
            this(aKey, "");
        }

        public StringProperty(String aKey, String aDefault)
        {
            super(aKey, aDefault);
        }

        @Override
        public String getValue()
        {
            if (Preferences.getInstance().containsKey(mKey))
            {
                return Preferences.getInstance().getString(mKey, mDefault);
            }

            Preferences.getInstance().putString(mKey, mDefault);
            return mDefault;
        }
    }

    public static class BooleanProperty extends IProperty<Boolean>
    {

        public BooleanProperty(String aKey)
        {
            this(aKey, false);
        }

        public BooleanProperty(String aKey, boolean aDefault)
        {
            super(aKey, aDefault);
        }

        @Override
        public Boolean getValue()
        {
            if (Preferences.getInstance().containsKey(mKey))
            {
                return Preferences.getInstance().getBoolean(mKey, mDefault);
            }

            Preferences.getInstance().putBoolean(mKey, mDefault);
            return mDefault;
        }
    }
}
